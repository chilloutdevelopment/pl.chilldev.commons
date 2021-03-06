<!---
# This file is part of the pl.wrzasq.commons.
#
# @license http://mit-license.org/ The MIT license
# @copyright 2015, 2019 © by Rafał Wrzeszcz - Wrzasq.pl.
-->

# HTML utilities

`pl.wrzasq.commons.text.html.Utils` class provides utility methods for *HTML* text processing.

## `firstParagraph()`

Returns content of the first paragraph (`<p>` element content, without wrapping `<p></p>` tags) in given *HTML* snippet:

```java
String shortContent = Utils.firstParagraph(siteContent);

System.out.println("<p class=\"short\">" + shortContent + "</p>");
```

When no paragraph can be found in provided snippet this method returns empty string.
