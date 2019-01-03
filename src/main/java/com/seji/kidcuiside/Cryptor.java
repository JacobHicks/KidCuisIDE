package com.seji.kidcuiside;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Cryptor {
    protected static String hash(String plaintext) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] digest = messageDigest.digest(plaintext.getBytes());
            StringBuilder hashbuilder = new StringBuilder();
            for (byte b : digest) {
                hashbuilder.append(String.format("%02X", b));
            }
            return hashbuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static String huffmanEncode(String data) {
        HashMap<Character, HuffNode> huffnodes = new HashMap<>();
        PriorityQueue<HuffNode> queue = new PriorityQueue<>();
        StringBuilder header = new StringBuilder();
        HuffNode tree;
        for(char c : data.toCharArray()) {
            if(huffnodes.containsKey(c)) {
                huffnodes.get(c).incVal();
            }
            else {
                huffnodes.put(c, new HuffNode(null, null, c, 1));
            }
        }
        for(char c : huffnodes.keySet()) {
            header.append(c).append(huffnodes.get(c).val).append("#");
            queue.add(huffnodes.get(c));
        }
        tree = generateTree(queue);
        StringBuilder builder = new StringBuilder();
        for(char c : data.toCharArray()) {
            builder.append(tree.search(c));
        }
        while(builder.length() % 16 != 0) {
            builder.append("0");
        }
        String binbody = builder.toString();
        StringBuilder body = new StringBuilder();
        for(int i = 0; i < binbody.length(); i+=16) {
            body.append((char)Integer.parseInt(binbody.substring(i, i+16), 2));
        }
        header.append("#").append(body);
        return header.toString();
    }

    protected static String huffmanDecode(String input) {
        PriorityQueue<HuffNode> queue = new PriorityQueue<>();
        Scanner scanner = new Scanner(input);
        HuffNode tree;
        HuffNode refrence;
        String output = "";
        String parsed = "";
        scanner.useDelimiter("");
        do {
            parsed = scanner.next();
            if(!parsed.equals("#")) {
                char data = parsed.charAt(0);
                scanner.useDelimiter("#");
                int val = scanner.nextInt();
                queue.offer(new HuffNode(null, null, data, val));
                scanner.useDelimiter("");
                scanner.next();
            }
        } while (!parsed.equals("#"));
        parsed = "";
        while(scanner.hasNext()) parsed += scanner.next();
        tree = generateTree(queue);
        refrence = tree;
        for(char c : parsed.toCharArray()) {
            String path = Integer.toString(c, 2);
            for(char pivot : path.toCharArray()) {
                if(refrence.data != null) {
                    output += refrence.data;
                    refrence = tree;
                }
                if(pivot == '0') refrence = refrence.left;
                else refrence = refrence.right;
            }
        }
        scanner.close();
        return output;
    }

    private static HuffNode generateTree(PriorityQueue<HuffNode> queue) {
        while(queue.size() > 1) {
            HuffNode left = queue.poll();
            HuffNode right = queue.poll();
            queue.offer(new HuffNode(left, right));
        }
        return queue.poll();
    }
}

class HuffNode implements Comparable<HuffNode> {
    HuffNode left;
    HuffNode right;
    Character data;
    int val;

    public HuffNode(HuffNode l, HuffNode r, char d, int v) {
        left = l;
        right = r;
        data = d;
        val = v;
    }

    public HuffNode(HuffNode l, HuffNode r) {
        left = l;
        right = r;
        val = l.val + r.val;
    }

    public void incVal() {
        val++;
    }

    public String search(char key) {
        if(data != null && data == key) return "";
        String searchleft = left != null ? left.search(key) : "-";
        String searchright = right != null ? right.search(key) : "-";
        if(!searchleft.equals("-")) return "0" + searchleft;
        else if(!searchright.equals("-")) return "1" + searchright;
        else return "-";
    }

    public int compareTo(HuffNode o) {
        return val - o.val;
    }
}