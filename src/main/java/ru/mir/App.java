package ru.mir;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {

        System.out.println("Подключение к главной странице...");

        Document doc = Jsoup.connect("https://ranobelib.me/omniscient-readers-viewpoint-novel/v1/c0")
                .userAgent("Mozilla")
                .get();

        System.out.println("Подключено");

        Element bookName = doc.getElementsByClass("reader-header-action__text text-truncate").get(0);

        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append(Tags.VERSION_ENCODING.getValue()).append("\n")
                .append(Tags.FICTION_BOOK_OPEN.getValue()).append("\n")
                .append(Tags.DESCRIPTION_OPEN.getValue()).append("\n")
                .append(Tags.TITLE_INFO_OPEN.getValue()).append("\n")
                .append(Tags.BOOK_TITLE_OPEN.getValue()).append("\n")
                .append(bookName.text()).append("\n")
                .append(Tags.BOOK_TITLE_CLOSE.getValue()).append("\n")
                .append(Tags.TITLE_INFO_CLOSE.getValue()).append("\n")
                .append(Tags.DESCRIPTION_CLOSE.getValue()).append("\n")
                .append(Tags.BODY_OPEN.getValue()).append("\n");

        StringBuilder contentStringBuilder = new StringBuilder();

        Element title = doc.getElementsByClass("reader-header-action__title text-truncate").get(1);

        StringBuilder chapterStringBuilder = new StringBuilder();
        chapterStringBuilder
                .append(Tags.SECTION_OPEN.getValue())
                .append(Tags.TITLE_OPEN.getValue())
                .append(title.text())
                .append(Tags.TITLE_CLOSE.getValue()).append("\n");

        Elements contentsList = doc.getElementsByClass("reader-container container container_center");
        Element content = contentsList.get(0);
        List<Node> divContentList = content.childNodes();
        divContentList.forEach(item -> chapterStringBuilder.append(item).append("\n"));

        chapterStringBuilder.append(Tags.SECTION_CLOSE.getValue()).append("\n");

        contentStringBuilder.append(chapterStringBuilder);

        Elements nextChapterList = doc.getElementsByClass("reader-next__btn button text-truncate button_label button_label_right");
        String nextChapterLink = nextChapterList.get(0).attr("href");

        while (nextChapterList.size() > 0) {
            Document nextDoc = Jsoup.connect(nextChapterLink)
                    .userAgent("Mozilla")
                    .get();

            Element titleNext = nextDoc.getElementsByClass("reader-header-action__title text-truncate").get(1);
//            System.out.println("Работа над главой: " + titleNext.text());

            StringBuilder chapterStringBuilderNext = new StringBuilder();
            chapterStringBuilderNext
                    .append(Tags.SECTION_OPEN.getValue())
                    .append(Tags.TITLE_OPEN.getValue())
                    .append(titleNext.text())
                    .append(Tags.TITLE_CLOSE.getValue()).append("\n");

            Elements contentsListNex = nextDoc.getElementsByClass("reader-container container container_center");
            Element contentNext = contentsListNex.get(0);
            List<Node> divContentListNext = contentNext.childNodes();
            divContentListNext.forEach(item -> chapterStringBuilderNext.append(item).append("\n"));

            chapterStringBuilderNext.append(Tags.SECTION_CLOSE.getValue()).append("\n");

            contentStringBuilder.append(chapterStringBuilderNext);

            nextChapterList = nextDoc.getElementsByClass("reader-next__btn button text-truncate button_label button_label_right");
            if (nextChapterList.size() > 0) {
                nextChapterLink = nextChapterList.get(0).attr("href");
            } else {
                break;
            }
        }

        bodyBuilder.append(contentStringBuilder);
        bodyBuilder
                .append(Tags.BODY_CLOSE.getValue()).append("\n")
                .append(Tags.FICTION_BOOK_CLOSE.getValue());

        String readyBook = bodyBuilder.toString();
        readyBook = readyBook.replace("&nbsp;"," ");

        BufferedWriter writer = new BufferedWriter(new FileWriter(bookName.text() + ".fb2"));
        writer.write(readyBook);
        writer.close();
    }
}
