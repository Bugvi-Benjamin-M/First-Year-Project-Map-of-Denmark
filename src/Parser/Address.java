package Parser;

import java.util.*;
import java.util.regex.*;

public class Address {

  private final String street, house, floor, side, postcode, city;
  protected Address(String _street, String _house, String _floor, String _side,
                    String _postcode, String _city) {
    street = _street;
    house = _house;
    floor = _floor;
    side = _side;
    postcode = _postcode;
    city = _city;
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (street != null && house != null) {
      builder.append(street).append(" ").append(house);
    }
    if (postcode != null || city != null) {
      if (builder.toString().length() > 0) {
        builder.append(",");
      }
      if (postcode != null) {
        builder.append(" ").append(postcode);
      }
      if (city != null) {
        builder.append(" ").append(city);
      }
    }
    return builder.toString();
  }

  public String street() { return street; }
  public String house() { return house; }
  public String floor() { return floor; }
  public String side() { return side; }
  public String postcode() { return postcode; }
  public String city() { return city; }

  public static Address parse(String s) {
    Builder b = new Builder();

    if (s == null || s.length() == 0) {
      return b.build();
    }

    HashMap<String, Callable> regex = Parser.getParser();

    for (Map.Entry<String, Callable> entry : regex.entrySet()) {
      String key = entry.getKey();
      Matcher matcher = Pattern.compile(key).matcher(s);
      if (matcher.matches()) {
        Callable cmd = entry.getValue();
        cmd.run(b, matcher);
        break;
      }
    }
    return b.build();
  }
}
