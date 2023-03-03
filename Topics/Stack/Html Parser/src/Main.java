import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String html = scanner.nextLine();

        // Just .push() and .pop() to simulate a stack
        Deque<String> tagStack = new ArrayDeque<>();
        Deque<Deque<String>> contentStack = new ArrayDeque<>();

        int i = 0;
        while (i < html.length()) {
            if (html.charAt(i) == '<') {
                String tagName = getTagName(html, i);
                // if the tag is a closing tag, pop and print top of content stack
                // push to content stack the content surrounded by the current tagName
                // pop the top of the tagStack
                if (tagName.charAt(0) == '/') {
                    Deque<String> contentQueue = contentStack.pop();
                    String joinedContent = joinContent(contentQueue);
                    System.out.println(joinedContent);

                    if (contentStack.size() > 0) {
                        contentStack.peek().push(tagStack.pop() + joinedContent + "<" + tagName + ">");
                    }
                } else {
                    tagStack.push("<" + tagName + ">");
                    contentStack.push(new ArrayDeque<String>());
                }
                i += tagName.length() + 2;
            } else {
                String content = getContent(html, i);
                if (contentStack.size() > 0) {
                    contentStack.peek().push(content);
                }
                i += content.length();
            }
        }
    }

    private static String joinContent(Deque<String> contentQueue) {
        StringBuilder joinedContent = new StringBuilder();
        while (contentQueue.size() > 0) {
            joinedContent.append(contentQueue.removeLast());
        }
        return joinedContent.toString();
    }

    private static String getTagName(String html, int index) {
        StringBuilder tagName = new StringBuilder();

        if (html.charAt(index) == '<') {
            index++;
        }

        while (html.charAt(index) != '>') {
            tagName.append(html.charAt(index));
            index++;
        }

        return tagName.toString();
    }

    private static String getContent(String html, int index) {
        StringBuilder content = new StringBuilder();

        while (html.charAt(index) != '<') {
            content.append(html.charAt(index));
            index++;
        }

        return content.toString();
    }
}