// 
// Decompiled by Procyon v0.5.30
// 

package ctf_problem;

class CTF_Problem
{
    public static Node root;
    public static int[] arr1;
    public static int[] arr2;
    static int l;
    static int m;
    
    public CTF_Problem() {
        CTF_Problem.root = null;
    }
    
    public boolean find(final int id) {
        Node current = CTF_Problem.root;
        while (current != null) {
            if (current.data == id) {
                return true;
            }
            if (current.data > id) {
                current = current.left;
            }
            else {
                current = current.right;
            }
        }
        return false;
    }
    
    public boolean delete(final int id) {
        Node parent = CTF_Problem.root;
        Node current = CTF_Problem.root;
        boolean isLeftChild = false;
        while (current.data != id) {
            parent = current;
            if (current.data > id) {
                isLeftChild = true;
                current = current.left;
            }
            else {
                isLeftChild = false;
                current = current.right;
            }
            if (current == null) {
                return false;
            }
        }
        if (current.left == null && current.right == null) {
            if (current == CTF_Problem.root) {
                CTF_Problem.root = null;
            }
            if (isLeftChild) {
                parent.left = null;
            }
            else {
                parent.right = null;
            }
        }
        else if (current.right == null) {
            if (current == CTF_Problem.root) {
                CTF_Problem.root = current.left;
            }
            else if (isLeftChild) {
                parent.left = current.left;
            }
            else {
                parent.right = current.left;
            }
        }
        else if (current.left == null) {
            if (current == CTF_Problem.root) {
                CTF_Problem.root = current.right;
            }
            else if (isLeftChild) {
                parent.left = current.right;
            }
            else {
                parent.right = current.right;
            }
        }
        else if (current.left != null && current.right != null) {
            final Node successor = this.getSuccessor(current);
            if (current == CTF_Problem.root) {
                CTF_Problem.root = successor;
            }
            else if (isLeftChild) {
                parent.left = successor;
            }
            else {
                parent.right = successor;
            }
            successor.left = current.left;
        }
        return true;
    }
    
    public Node getSuccessor(final Node deleleNode) {
        Node successsor = null;
        Node successsorParent = null;
        for (Node current = deleleNode.right; current != null; current = current.left) {
            successsorParent = successsor;
            successsor = current;
        }
        if (successsor != deleleNode.right) {
            successsorParent.left = successsor.right;
            successsor.right = deleleNode.right;
        }
        return successsor;
    }
    
    public void insert(final int id) {
        final Node newNode = new Node(id);
        if (CTF_Problem.root == null) {
            CTF_Problem.root = newNode;
            return;
        }
        Node current = CTF_Problem.root;
        Node parent = null;
        while (true) {
            parent = current;
            if (id < current.data) {
                current = current.left;
                if (current == null) {
                    parent.left = newNode;
                    return;
                }
                continue;
            }
            else {
                current = current.right;
                if (current == null) {
                    parent.right = newNode;
                    return;
                }
                continue;
            }
        }
    }
    
    public void inOrder(final Node root) {
        if (root != null) {
            this.inOrder(root.left);
            CTF_Problem.arr1[CTF_Problem.l++] = root.data;
            this.inOrder(root.right);
        }
    }
    
    void postOrder(final Node node) {
        if (node == null) {
            return;
        }
        this.postOrder(node.left);
        this.postOrder(node.right);
        CTF_Problem.arr2[CTF_Problem.m++] = node.data;
    }
    
    public int[] getResultA(final int[] a) {
        final CTF_Problem b = new CTF_Problem();
        CTF_Problem.l = (CTF_Problem.m = 0);
        for (int i = 0; i < CTF_Problem.arr1.length; ++i) {
            b.insert(a[i]);
        }
        b.inOrder(CTF_Problem.root);
        return CTF_Problem.arr1;
    }
    
    public int[] getResultB(final int[] a) {
        final CTF_Problem b = new CTF_Problem();
        CTF_Problem.l = (CTF_Problem.m = 0);
        for (int i = 0; i < CTF_Problem.arr2.length; ++i) {
            b.insert(a[i]);
        }
        b.postOrder(CTF_Problem.root);
        return CTF_Problem.arr2;
    }
    
    static {
        CTF_Problem.arr1 = new int[100];
        CTF_Problem.arr2 = new int[100];
    }
}
