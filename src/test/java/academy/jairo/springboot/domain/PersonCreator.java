package academy.jairo.springboot.domain;

public class PersonCreator {

    public static Person create() {
        return Person.builder()
                .name("John Power")
                .build();
    }

    public static Person create(String name) {
        return Person.builder()
                .name(name)
                .build();
    }

    public static Person createValid() {
        return Person.builder()
                .name("John Power")
                .id(1L)
                .build();
    }

    public static Person createValidUpdated() {
        return Person.builder()
                .name("John Power Dois")
                .id(1L)
                .build();
    }

}
