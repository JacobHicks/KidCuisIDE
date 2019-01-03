package com.seji.kidcuiside;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(Model model, @CookieValue(value = "id", defaultValue = "") String id) {
        FileData test = new FileData(0, "Cryptor", "package com.seji.kidcuiside;\n" +
                "\n" +
                "import java.math.BigInteger;\n" +
                "import java.security.MessageDigest;\n" +
                "import java.security.NoSuchAlgorithmException;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.PriorityQueue;\n" +
                "import java.util.Scanner;\n" +
                "\n" +
                "public class Cryptor {\n" +
                "    protected static String hash(String plaintext) {\n" +
                "        try {\n" +
                "            MessageDigest messageDigest = MessageDigest.getInstance(\"SHA-512\");\n" +
                "            byte[] digest = messageDigest.digest(plaintext.getBytes());\n" +
                "            StringBuilder hashbuilder = new StringBuilder();\n" +
                "            for (byte b : digest) {\n" +
                "                hashbuilder.append(String.format(\"%02X\", b));\n" +
                "            }\n" +
                "            return hashbuilder.toString();\n" +
                "        } catch (NoSuchAlgorithmException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    protected static String huffmanEncode(String data) {\n" +
                "        HashMap<Character, HuffNode> huffnodes = new HashMap<>();\n" +
                "        PriorityQueue<HuffNode> queue = new PriorityQueue<>();\n" +
                "        StringBuilder header = new StringBuilder();\n" +
                "        HuffNode tree;\n" +
                "        for(char c : data.toCharArray()) {\n" +
                "            if(huffnodes.containsKey(c)) {\n" +
                "                huffnodes.get(c).incVal();\n" +
                "            }\n" +
                "            else {\n" +
                "                huffnodes.put(c, new HuffNode(null, null, c, 1));\n" +
                "            }\n" +
                "        }\n" +
                "        for(char c : huffnodes.keySet()) {\n" +
                "            header.append(c).append(huffnodes.get(c).val).append(\"#\");\n" +
                "            queue.add(huffnodes.get(c));\n" +
                "        }\n" +
                "        tree = generateTree(queue);\n" +
                "        StringBuilder builder = new StringBuilder();\n" +
                "        for(char c : data.toCharArray()) {\n" +
                "            builder.append(tree.search(c));\n" +
                "        }\n" +
                "        while(builder.length() % 8 != 0) {\n" +
                "            builder.append(\"0\");\n" +
                "        }\n" +
                "        String binbody = builder.toString();\n" +
                "        StringBuilder body = new StringBuilder();\n" +
                "        for(int i = 0; i < binbody.length(); i+=8) {\n" +
                "            body.append((char)(Integer.parseInt(binbody.substring(i, i+8), 2)));\n" +
                "        }\n" +
                "        header.append(\"#\").append(body);\n" +
                "        return header.toString();\n" +
                "    }\n" +
                "\n" +
                "    protected static String huffmanDecode(String input) {\n" +
                "        PriorityQueue<HuffNode> queue = new PriorityQueue<>();\n" +
                "        Scanner scanner = new Scanner(input);\n" +
                "        HuffNode tree;\n" +
                "        HuffNode refrence;\n" +
                "        String output = \"\";\n" +
                "        String parsed = \"\";\n" +
                "        scanner.useDelimiter(\"\");\n" +
                "        int length = 0;\n" +
                "        do {\n" +
                "            parsed = scanner.next();\n" +
                "            if(!parsed.equals(\"#\")) {\n" +
                "                char data = parsed.charAt(0);\n" +
                "                scanner.useDelimiter(\"#\");\n" +
                "                int val = scanner.nextInt();\n" +
                "                queue.offer(new HuffNode(null, null, data, val));\n" +
                "                scanner.useDelimiter(\"\");\n" +
                "                scanner.next();\n" +
                "            }\n" +
                "        } while (!parsed.equals(\"#\"));\n" +
                "        parsed = \"\";\n" +
                "        while(scanner.hasNext()) parsed += scanner.next();\n" +
                "        tree = generateTree(queue);\n" +
                "        refrence = tree;\n" +
                "        for(char c : parsed.toCharArray()) {\n" +
                "            String path = String.format(\"%8s\", Integer.toString(c, 2)).replace(' ', '0');\n" +
                "            for(char pivot : path.toCharArray()) {\n" +
                "                if(refrence.val != 0) {\n" +
                "                    if (refrence.data != null) {\n" +
                "                        output += refrence.data;\n" +
                "                        refrence.val--;\n" +
                "                        refrence = tree;\n" +
                "                    }\n" +
                "                    if (pivot == '0') refrence = refrence.left;\n" +
                "                    else refrence = refrence.right;\n" +
                "                    length--;\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        scanner.close();\n" +
                "        return output;\n" +
                "    }\n" +
                "\n" +
                "    private static HuffNode generateTree(PriorityQueue<HuffNode> queue) {\n" +
                "        while(queue.size() > 1) {\n" +
                "            HuffNode left = queue.poll();\n" +
                "            HuffNode right = queue.poll();\n" +
                "            queue.offer(new HuffNode(left, right));\n" +
                "        }\n" +
                "        return queue.poll();\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "class HuffNode implements Comparable<HuffNode> {\n" +
                "    HuffNode left;\n" +
                "    HuffNode right;\n" +
                "    Character data;\n" +
                "    int val;\n" +
                "\n" +
                "    public HuffNode(HuffNode l, HuffNode r, char d, int v) {\n" +
                "        left = l;\n" +
                "        right = r;\n" +
                "        data = d;\n" +
                "        val = v;\n" +
                "    }\n" +
                "\n" +
                "    public HuffNode(HuffNode l, HuffNode r) {\n" +
                "        left = l;\n" +
                "        right = r;\n" +
                "        val = l.val + r.val;\n" +
                "    }\n" +
                "\n" +
                "    public void incVal() {\n" +
                "        val++;\n" +
                "    }\n" +
                "\n" +
                "    public String search(char key) {\n" +
                "        if(data != null && data == key) return \"\";\n" +
                "        String searchleft = left != null ? left.search(key) : \"-\";\n" +
                "        String searchright = right != null ? right.search(key) : \"-\";\n" +
                "        if(!searchleft.equals(\"-\")) return \"0\" + searchleft;\n" +
                "        else if(!searchright.equals(\"-\")) return \"1\" + searchright;\n" +
                "        else return \"-\";\n" +
                "    }\n" +
                "\n" +
                "    public int compareTo(HuffNode o) {\n" +
                "        return val - o.val;\n" +
                "    }\n" +
                "}");
        String tsts = test.serialize();
        System.out.println(tsts);
        System.out.println(test.getData().length() + " " + tsts.length() + "  " + (double)tsts.length()/test.getData().length());
        test = new FileData(tsts);
        System.out.println(test.getData());
        if(id.equals("")) {
            model.addAttribute("login", new LoginBean());
            return "login";
        }
        else {  //TODO check if id is valid
            return "redirect:/code";
        }
    }

    @PostMapping("/login")
    public String loginRequest(@ModelAttribute LoginBean login, HttpServletResponse response) {
        String user = login.getUsername();
        String password = login.getPassword();
        if(!isValidInput(user, password)) {
            return "redirect:/login";
        }
        else {
            String hash = Cryptor.hash("Kid" + user + "Cuis" + password + "IDE");
            response.addCookie(new Cookie("id", hash));
            //TODO check hash against DB
        }
        return "redirect:/code";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("signup", new SignUpBean());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute SignUpBean signup) {
        if(signup.getPassword().equals(signup.getConfirmPassword()) && isValidInput(signup.getUsername(), signup.getPassword())) {
            String hash = Cryptor.hash("Kid" + signup.getUsername() + "Cuis" + signup.getPassword() + "IDE");
            return "redirect:/signupconfirm";
            //TODO register user in db
        }
        //Try again skrub
        return "redirect:/signup";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        response.addCookie(new Cookie("id", ""));
        return "redirect:/login";
    }

    private boolean isValidInput(String user, String password) {
        return !(user == null || user.length() < 5 || user.length() > 64 || user.contains("\\W") || (user+password).contains("\\s") || password == null || password.length() < 6 || password.length() > 128);
    }
}