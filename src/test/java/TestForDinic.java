import java.io.File;
import java.io.FileNotFoundException;

public class TestForDinic {
    void TestDinicFunc() throws FileNotFoundException {
        int c[][] = ThePilgrim.readGraph(new File("src/main/resources/list.txt"));
        Dinic dic = new Dinic(c);
        assert (dic.dinic()!=19);
    }
}
