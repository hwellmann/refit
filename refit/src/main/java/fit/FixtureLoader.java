package fit;


/**
 * Loads fixture classes and creates fixture instances. This is a singleton which can be
 * replaced by a derive object to override the class loading or instantiation behaviour.
 * 
 * @author Harald Wellmann
 *
 */
public class FixtureLoader {
    private static FixtureLoader instance;

    /**
     * Returns the singleton instance.
     * @return the fixture loader
     */
    public static FixtureLoader getInstance() {
        if (instance == null) {
            instance = new FixtureLoader();
        }

        return instance;
    }

    /**
     * Installs the singleton instance to be used. This should only be called to override
     * the default singleton by an instance of a derived class.
     * @param instance
     */
    public static void setInstance(FixtureLoader instance) {
        FixtureLoader.instance = instance;
    }

    /**
     * Loads a fixture class with the given class name. By default, the thread context class
     * loader is used. Override this to use other class loaders (e.g. in OSGi).
     * @param className fully qualified class name
     * @return fixture class
     * @throws ClassNotFoundException
     */
    public Class<?> loadFixtureClass(String className) throws ClassNotFoundException {
        Class<?> klass = Thread.currentThread().getContextClassLoader().loadClass(className);
        return klass;
    }
    
    /**
     * Creates an instance of the given fixture class. Override this to postprocess the fixture
     * instance, e.g. for dependency injection.
     * @param klass fixture class
     * @return fixture instance
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Fixture createFixture(Class<?> klass) throws InstantiationException, IllegalAccessException {
        Fixture fixture = (Fixture) klass.newInstance();
        return fixture;
    }
}
