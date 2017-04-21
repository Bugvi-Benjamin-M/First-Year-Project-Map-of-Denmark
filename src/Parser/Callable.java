package Parser;
import java.util.*;
import java.util.regex.*;

interface Callable {
  void run(Builder b, Matcher m);
}
