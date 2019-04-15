package com.seji.kidcuiside;

public class Tester {
    public static void main(String[] args) {
        CodeWindowController cwc = new CodeWindowController();
        Code testCode = new Code();
        testCode.setCode("import java.util.Scanner;\n" +
                "\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        Scanner in = new Scanner(System.in);\n" +
                "        System.out.print(\"Enter Input: \"); System.out.println(\"\\n\" + in.nextLine());\n" +
                "    }\n" +
                " }");
        testCode.setRequest("save");
        System.out.println(cwc.getCode(testCode, "66E7EC77C192D7D176A42683B933925E0B386066B65913895E1B88B8BF2870EDFD8A86DE1F1F90748A6EA691F5F51844074043936AF0E82175F3D20D953516E2", "jacobhicks"));
        testCode.setRequest("run");
        System.out.println(cwc.getCode(testCode, "66E7EC77C192D7D176A42683B933925E0B386066B65913895E1B88B8BF2870EDFD8A86DE1F1F90748A6EA691F5F51844074043936AF0E82175F3D20D953516E2", "jacobhicks"));
    }
}