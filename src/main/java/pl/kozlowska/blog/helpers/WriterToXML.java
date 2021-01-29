package pl.kozlowska.blog.helpers;

import java.io.*;

public class WriterToXML {
    public static void createXMLFromCSV() {
        try {
            PrintWriter printWriter = new PrintWriter("src/main/resources/beans.xml");
            printWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            printWriter.println("<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                    "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd\">\n");
            writeToXML(printWriter, new BufferedReader(new FileReader("src/main/resources/csvFiles/PostsAuthorsId.csv")), "PostsAuthorsId");
            writeToXML(printWriter, new BufferedReader(new FileReader("src/main/resources/csvFiles/Author.csv")), "Author");
            writeToXML(printWriter, new BufferedReader(new FileReader("src/main/resources/csvFiles/Post.csv")), "Post");
            writeToXML(printWriter, new BufferedReader(new FileReader("src/main/resources/csvFiles/Comment.csv")), "Comment");
            writeToXML(printWriter, new BufferedReader(new FileReader("src/main/resources/csvFiles/Attachment.csv")), "Attachment");
            printWriter.println("</beans>");
            printWriter.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToXML(PrintWriter printWriter, BufferedReader br, String fileName) throws IOException {
        String line = "";
        if ((line = br.readLine()) != null) {
            String[] head = line.split(",");

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
                printWriter.println("\t<bean class=\"pl.kozlowska.blog.models."+fileName+"\">");
                for (int i = 0; i < head.length; i++) {
                    printWriter.println("\t\t<constructor-arg index=\""+i+"\" value=\""+values[i].replaceAll("\"", "")+"\" />");
                }
                printWriter.println("\t</bean>\n");
            }
        }
    }
}
