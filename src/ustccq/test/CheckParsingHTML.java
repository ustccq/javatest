package ustccq.test;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CheckParsingHTML {
	final static Set<String> tagsStrings = new HashSet<String>(){{
		add("#root");
		add("body");
		add("head");
		add("html");
	}};
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String html = "<html>\n" +
        "<body>\n" +
        "\n" +
        "<a href=\"http://www.google.com\">\n" +
        "世界那么 大用谷歌看看</a>\n" +
        "<a href=\"http://www.baidu.com\">\n" +
        "世界那么 大用百度看看</a>\n" +
        "<button type='button'>\n" +
        "点一下看看</button >\n" +
        "\n" +
        "</body>\n" +
        "</html>";
		printElement(html);
	}
	private static void printElement(String html) {
		Document result = Jsoup.parse(html);
		Elements eles = result.getAllElements();
//		System.out.println(result.body().text());
		
		for(Element element : eles) {
			if (tagsStrings.contains(element.tagName()))
				continue;
			
			System.out.println(element.tagName());
			if (element.hasText()) {
				System.out.println("=====================================================" + element.tagName());
				System.out.println(element.text());
				System.out.println(element.outerHtml());
				System.out.println("=====================================================" + element.tagName());
				System.out.println();
			}
		}
	}
}
