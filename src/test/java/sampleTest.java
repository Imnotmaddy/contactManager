import app.models.Contact;
import app.sql.dao.mysql.ContactDaoImpl;
import app.sql.pool.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class sampleTest {

    @Mock
    Connection mockConnection;

    @Mock
    ConnectionPool mockPool;

    @Mock
    ContactDaoImpl contactDao;

    private final String DB_URL = "jdbc:mysql://localhost:3306/contactManager?useSSL=false&autoReconnect=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8";
    private final String DB_USER = "root";

    private final String DB_PASSWORD = "1234";

    private final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPool() throws Exception {
        ContactDaoImpl dummy = Mockito.mock(ContactDaoImpl.class);
        when(dummy.findAll()).thenReturn(new ArrayList<>());
    }

    @Test
    void testFindById() throws Exception{
        ContactDaoImpl dummy = Mockito.mock(ContactDaoImpl.class);
        when(dummy.findById(anyInt())).thenReturn(new Contact());
    }

    @Test
    void testGetConnection() throws Exception{
        when(mockPool.getConnection()).thenReturn(mockConnection);
        when(contactDao.save(any(Contact.class))).thenReturn(new Contact());
    }

    @Test
    void test4() throws Exception{
        Contact contact = new Contact("123","123","123");
        when(contactDao.findById(182)).thenReturn(contact);
    }


}
