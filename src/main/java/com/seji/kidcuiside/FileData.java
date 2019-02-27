package com.seji.kidcuiside;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileData {
    private int id;
    private String name;
    private String data;

    public FileData() {}

    public FileData(int i, String n, String d) {
        id = i;
        name = n;
        data = d;
    }

    public FileData(String code) {
        name = "";
        data = "";
        Scanner in = new Scanner(code);
        in.useDelimiter("#");
        int namelen = in.nextInt();
        in.useDelimiter("");
        in.next();
        while (namelen-- > 0) {
            name += in.next();
        }
        in.useDelimiter("#");
        id = in.nextInt();
        in.useDelimiter("");
        in.next();
        StringBuilder body = new StringBuilder();
        while(in.hasNext()) {
            body.append(in.next());
        }
        in.close();
        data = Cryptor.huffmanDecode(body.toString());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String serialize() {
        return name.length() + "#" + name + "#" + id + "#" + Cryptor.huffmanEncode(data);
    }

    public void write(String location) {
        try {
            new File(location).mkdirs();
            PrintWriter out = new PrintWriter(new File(location + "/" + id + name));
            out.print(serialize());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "FileData{" +
                "\nid=" + id +
                "\n, name='" + name + '\'' +
                "\n, data='" + data + '\'' +
                "\n}";
    }
}
