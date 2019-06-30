
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] inOrder = { 9, 11, 33, 35, 38, 40, 44, 48, 61, 85, 89, 101, 106, 110, 135, 150, 159, 180, 188, 200, 201, 214, 241, 253, 268, 269, 275, 278, 285, 301, 301, 327, 356, 358, 363, 381, 396, 399, 413, 428, 434, 445, 449, 462, 471, 476, 481, 492, 496, 497, 509, 520, 526, 534, 540, 589, 599, 613, 621, 621+1, 623, 628, 634, 650, 652, 653, 658, 665, 679, 691, 708, 711, 716, 722, 752, 756, 764, 771, 773, 786, 807, 808, 826, 827, 836, 842, 856, 867, 875, 877, 879, 889, 892, 922, 946, 951, 965, 980, 993, 996 };
        int[] postOrder = { 35, 33, 44, 40, 38, 48, 11, 85, 89, 61, 110, 150, 159, 135, 188, 200, 180, 106, 101, 214, 268, 275, 269, 253, 241, 201, 9, 301, 301, 285, 327, 356, 363, 396, 413, 399, 445, 434, 462, 449, 428, 471, 481, 492, 496, 497, 476, 381, 358, 278, 534, 526, 520, 613, 599, 623, 621+1, 621, 589, 540, 628, 650, 653, 652, 665, 691, 679, 711, 756, 752, 722, 716, 807, 786, 773, 771, 826, 808, 827, 764, 856, 875, 867, 842, 836, 708, 879, 892, 889, 922, 877, 951, 946, 658, 980, 996, 993, 965, 634, 509 };
        Node result = recover(inOrder, postOrder);
        preOrder(result);
        System.out.println("over");
    }

    public static Node recover(int[] inOrder, int[] postOrder){
    	if(inOrder.length == 1 || postOrder.length == 1) {
    		Node nowNode = new Node(postOrder[0]);
    		nowNode.left = null;
    		nowNode.right = null;
    		return nowNode;
    	}else if(inOrder.length == 0 || postOrder.length == 0) {
    		return null;
    	}
        Node nowNode = new Node(postOrder[postOrder.length - 1]);

        int position = -1;
        for (int i = 0; i < inOrder.length; i++){
            if(nowNode.data == inOrder[i]){
                position = i;
            }
        }
        nowNode.left = recover(Arrays.copyOfRange(inOrder, 0, position), Arrays.copyOfRange(postOrder, 0, position));
        nowNode.right = recover(Arrays.copyOfRange(inOrder, position + 1, inOrder.length), Arrays.copyOfRange(postOrder, position, postOrder.length - 1));

        return nowNode;
    }
    
    public static void preOrder(Node n) {
    	if(n == null) {
    		return ;
    	}
    	System.out.print(" " + n.data);
    	preOrder(n.left);
    	preOrder(n.right);
    }
}

class Node
{
    int data;
    Node left;
    Node right;
    
    public Node(final int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}