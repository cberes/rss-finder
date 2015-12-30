# rss-finder

Simple utility to find and format an item in an RSS feed.

## Installation

Clone the repo. Use [Leiningen](http://leiningen.org/) to build the standalone JAR.

    $ lein uberjar

## Usage

Run the standalone JAR file.

    $ java -jar rss-finder-0.1.0-standalone.jar

You will need a configuration file. See the next section.

## Options

Rss-finder requires a configuration file. It uses [edn](https://github.com/edn-format/edn) for the configuration file. The file must be located at `$HOME/.rss-finder/config.edn`.

These are the available options:
- `:rss-url` defines the URL of an RSS feed to parse
- `:rss-file` specifies the path to a local RSS file to parse
- `:title-regex` is used to pick the first item with a matching title. The regex is enclosed in `\b` classes
- `:description-regex` is used to select text from the item's description. It should define at least one capturing group (which will be used with the `:format-text` option)
- `:format-text` specifies a Java format string that is output if a matching RSS item is found. The capturing groups from the result of applying the `:description-regex` option are used as the arguments to format this text
- `:extra-output` sets extra text to print before the program exits

See the following section for an example configuration.

## Examples

This is an example configuration:

```clojure
{
  :rss-url "http://sabres.nhl.com/rss/news.xml"
  :title-regex "against"
  :description-regex "(.*?) +- +.+"
  :format-text "Tonight's game is in %s."
  :extra-output "Good luck!"
}
```

With the above configuration, the Buffalo Sabres' RSS feed would be read. The program would find the first item that contains the word "against." It would find a word following by at least one space, a dash, at least one space, and then any character.

For example, if the feed contained this item:

```xml
<item>
  <title>SABRES LOOK TO REBOUND AGAINST STRUGGLING CANUCKS</title>
  <link>
    <![CDATA[
    http://sabres.nhl.com/club/news.htm?id=791536&cmpid=rss-labarber
    ]]>
  </link>
  <description>
    <![CDATA[
    VANCOUVER - Linus Ullmark will start in net for the Buffalo Sabres as they face a struggling Vancouver Canucks team on Monday night at Rogers Arena. Buffalo is looking to rebound from a 4-2 loss in Edmonton on Sunday.

    The Canucks, meanwhile, ar...
    ]]>
  </description>
  <author>jourdon.labarber@sabres.com</author>
  <pubDate>Mon, 07 Dec 2015 20:48:00 EST</pubDate>
  <guid><![CDATA[ http://sabres.nhl.com/club/news.htm?id=791536 ]]></guid>
</item>
```

Then the program would output this text.

```
Tonight's game is in VANCOUVER.
Good luck!
```

### Notes

This tool is not meant to poll an RSS feed frequently.

## License

Copyright © 2015 Corey Beres

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
