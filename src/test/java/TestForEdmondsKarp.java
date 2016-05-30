import java.io.File;
import java.io.FileNotFoundException;

public class TestForEdmondsKarp {
    public void TestEdmondsFunction() throws FileNotFoundException {
        int c[][] = ThePilgrim.readGraph(new File("src/main/resources/list.txt"));
        EdmondsKarp ed = new EdmondsKarp(c);
        assert (ed.edmondsKarp()!=19);
    }
}
