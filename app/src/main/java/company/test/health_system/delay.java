package company.test.health_system;

public class delay extends Thread {
    int de;
    delay(int de)
    {
        this.de=de;
    }

    @Override
    public void run() {
        try
        {
            sleep(de);
        }catch (Exception e)
        {

        }

    }
}
