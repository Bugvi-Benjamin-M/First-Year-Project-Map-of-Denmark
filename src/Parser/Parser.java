package Parser;

import java.util.*;
import java.util.regex.*;

class Parser {
    protected static HashMap<String, Callable> getParser()
    {
        HashMap<String, Callable> regex = new HashMap<String, Callable>();
        regex.put(
            "(?<street>[A-Za-zæøåÆØÅ ]+) (?<house>[0-9]+), (?<postcode>[0-9]{4}) (?<city>[A-Za-zæøåÆØÅ ]+)",
            new Callable() {
                public void run(Builder b, Matcher m)
                {
                    b.street(m.group("street"))
                        .house(m.group("house"))
                        .postcode(m.group("postcode"))
                        .city(m.group("city"));
                };
            });

        regex.put(
            "(?<street>[A-Za-zæøåÆØÅ ]+) (?<house>[0-9]+), (?<postcode>[0-9]{4})",
            new Callable() {
                public void run(Builder b, Matcher m)
                {
                    b.street(m.group("street"))
                        .house(m.group("house"))
                        .postcode(m.group("postcode"));
                };
            });

        regex.put("(?<street>[A-Za-zæøåÆØÅ ]+) (?<house>[0-9]+)", new Callable() {
            public void run(Builder b, Matcher m)
            {
                b.street(m.group("street")).house(m.group("house"));
            };
        });
        return regex;
    }
}
