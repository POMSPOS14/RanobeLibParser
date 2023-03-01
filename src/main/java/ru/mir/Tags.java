package ru.mir;

public enum Tags {

    VERSION_ENCODING("<?xml version=\"1.0\" encoding=\"utf-8\"?>"),
    FICTION_BOOK_OPEN("<FictionBook xmlns:l=\"http://www.w3.org/1999/xlink\" xmlns=\"http://www.gribuser.ru/xml/fictionbook/2.0\">"),
    FICTION_BOOK_CLOSE("</FictionBook>"),
    BOOK_TITLE_OPEN("<book-title>"),
    BOOK_TITLE_CLOSE("</book-title>"),
    TITLE_OPEN("<title>"),
    TITLE_CLOSE("</title>"),
    TITLE_INFO_OPEN("<title-info>"),
    TITLE_INFO_CLOSE("</title-info>"),
    DESCRIPTION_OPEN("<description>"),
    DESCRIPTION_CLOSE("</description>"),
    SECTION_OPEN("<section>"),
    SECTION_CLOSE("</section>"),
    BODY_OPEN("<body>"),
    BODY_CLOSE("</body>");


    public final String value;

    private Tags(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
