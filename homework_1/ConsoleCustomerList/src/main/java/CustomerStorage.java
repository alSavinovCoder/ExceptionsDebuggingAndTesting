import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerStorage {
    private final Map<String, Customer> storage;

    public CustomerStorage() {
        storage = new HashMap<>();
    }

    public void addCustomer(String data) {
        final int INDEX_NAME = 0;
        final int INDEX_SURNAME = 1;
        final int INDEX_EMAIL = 2;
        final int INDEX_PHONE = 3;


        String[] components = data.split("\\s+");

        Pattern emailPattern =
                Pattern.compile("^((\\w|[-+])+(\\.[\\w-]+)*@[\\w-]+((\\.[\\d\\p{Alpha}]+)*(\\.\\p{Alpha}{2,})*)*)$");
        Matcher emailMatcher = emailPattern.matcher(components[INDEX_EMAIL]);
        Pattern phonePattern = Pattern.compile("^((\\+7)([0-9]{10}))$");
        Matcher phoneMatcher = phonePattern.matcher(components[INDEX_PHONE]);

        if (emailMatcher.matches() == false) {
            throw new IllegalArgumentException("Некорректная запись электронной почты");
        }
        if (phoneMatcher.matches() == false) {
            throw new IllegalArgumentException("Некорректная запись номера телефона");
        }
        if (components.length != 4) {
            throw new IllegalArgumentException("Некорректный формат записи информации о клиенте");
        }

        String name = components[INDEX_NAME] + " " + components[INDEX_SURNAME];
        storage.put(name, new Customer(name, components[INDEX_PHONE], components[INDEX_EMAIL]));
    }

    public void listCustomers() {
        storage.values().forEach(System.out::println);
    }

    public void removeCustomer(String name) {
        storage.remove(name);
    }

    public Customer getCustomer(String name) {
        return storage.get(name);
    }

    public int getCount() {
        return storage.size();
    }
}