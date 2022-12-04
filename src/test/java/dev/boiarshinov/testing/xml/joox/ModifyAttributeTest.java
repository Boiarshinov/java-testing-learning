package dev.boiarshinov.testing.xml.joox;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static org.joox.JOOX.$;

public class ModifyAttributeTest {

    public static final String XML =
        """
        <?xml version="1.0" encoding="UTF-8"?>
        <document>
          <library name="Amazon">
            <books>
              <book id="1">
                <name>1984</name>
                <authors>
                  <author>George Orwell</author>
                </authors>
              </book>
              <book id="2">
                <name>Animal Farm</name>
                <authors>
                  <author>George Orwell</author>
                </authors>
              </book>
              <book id="3">
                <name>O Alquimista</name>
                <authors>
                  <author>Paulo Coelho</author>
                </authors>
              </book>
              <book id="4">
                <name>Brida</name>
                <authors>
                  <author>Paulo Coelho</author>
                </authors>
              </book>
            </books>
                    
            <dvds>
              <dvd id="5">
                <name>Once Upon a Time in the West</name>
                <directors>
                  <director>Sergio Leone</director>
                </directors>
                <actors>
                  <actor>Charles Bronson</actor>
                  <actor>Jason Robards</actor>
                  <actor>Claudia Cardinale</actor>
                </actors>
              </dvd>
            </dvds>
          </library>
        </document>
        """;

    @Test
    void test() { //todo not a test, lol
        Document document = $(XML).document();

        String content = $(document).find("book")
            .filter(context -> $(context).find("name").content().equals("1984"))
            .attr("id", "10")
            .toString();

        System.out.println(content);


        $(document).find("library").attr("name", "Bookmate");
        System.out.println($(document).toString());
    }
}
