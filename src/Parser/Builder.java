package Parser;

class Builder {
  protected String street = null, house = null, floor = null, side = null,
                 postcode = null, city = null;
  protected Builder street(String _street) {
    street = _street;
    return this;
  }
  protected Builder house(String _house) {
    house = _house;
    return this;
  }
  protected Builder floor(String _floor) {
    floor = _floor;
    return this;
  }
  protected Builder side(String _side) {
    side = _side;
    return this;
  }
  protected Builder postcode(String _postcode) {
    postcode = _postcode;
    return this;
  }
  protected Builder city(String _city) {
    city = _city;
    return this;
  }
  protected Address build() {
    return new Address(street, house, floor, side, postcode, city);
  }
}
