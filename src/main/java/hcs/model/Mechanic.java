package hcs.model;

/**
 * Mecânicas do hearthstone.
 * 
 * @author 99689650068
 *
 */
@Deprecated(since = "Use Tag class")
public class Mechanic extends Entity {
    public String regex, cathegory;
    // Map<Mechanic, Float> aff = new LinkedHashMap<Mechanic, Float>();

    public Mechanic(String id, String regex) {
	this.id = id;
	this.regex = regex;
    }

    @Override
    public String toString() {
	return this.regex;
    }

    public enum MECANICAS {

    }
}