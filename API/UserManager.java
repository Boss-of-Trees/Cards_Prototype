package API;

public class UserManager
{

    private SQLiteManager sqLiteManager;

    public UserManager()
    {
        sqlManagement();
    }

    private void sqlManagement()
    {
        sqLiteManager = new SQLiteManager();
        sqLiteManager.connect();
        sqLiteManager.createUserTable();
    }

    public RegisterEnum registerUser(String username, String password)
    {
        if(!sqLiteManager.userExists(username))
        {
            if(sqLiteManager.registerUser(username, password))
            {
                return RegisterEnum.WORKED;
            }
            else
            {
                return RegisterEnum.SQL_ERROR;
            }
        }
        else
        {
            return RegisterEnum.USERNAME_USED;
        }
    }

    public boolean resetTable()
    {
        return sqLiteManager.resetTable();
    }
}