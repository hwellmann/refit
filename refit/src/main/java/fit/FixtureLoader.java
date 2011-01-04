package fit;


public class FixtureLoader {
    private static FixtureLoader instance;

    public static FixtureLoader getInstance() {
        if (instance == null) {
            instance = new FixtureLoader();
        }

        return instance;
    }

    public static void setInstance(FixtureLoader instance) {
        FixtureLoader.instance = instance;
    }

    public Class<?> loadFixtureClass(String className) throws ClassNotFoundException {
        Class<?> klass = Thread.currentThread().getContextClassLoader().loadClass(className);
        return klass;
    }
    
    public Fixture createFixture(Class<?> klass) throws InstantiationException, IllegalAccessException {
        Fixture fixture = (Fixture) klass.newInstance();
        return fixture;
    }
}
