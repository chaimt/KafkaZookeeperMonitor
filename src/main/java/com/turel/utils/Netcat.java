package com.turel.utils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * Created by chaimturkel on 12/7/16.
 */
public class Netcat {

    public static void main(String[] args) throws UnknownHostException, IOException {
        //Check input parameters
        if (args.length != 2) {
            System.err.println("Invalid parameters");
            System.out.println("Usage: Netcat [hostname] [port]");
            System.exit(1);
        }

        run(args[0], Integer.parseInt(args[1]), System.in, System.out);
    }

    public static String run(String command, String host, int port) throws UnknownHostException, IOException {
        InputStream stream = new ByteArrayInputStream(command.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        run(host,port,stream,baos);
        return baos.toString();
    }

    public static void run(String host, int port, InputStream input, OutputStream output) throws UnknownHostException, IOException {
        //Create and open socket
        Socket socket = new Socket(host, port);
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        //Push, pull, close.


        FSHelper.copy(input, out);
        FSHelper.copy(in, output);
        FSHelper.closeQuietly(socket);
    }
}
