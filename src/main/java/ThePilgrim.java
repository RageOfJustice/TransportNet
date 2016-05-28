import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ThePilgrim extends JFrame {

    private JMenuBar mainMenu = new JMenuBar();

    private JMenu file = new JMenu("Файл");
    private JMenu openGraphFromFile = new JMenu("Граф");

    private JMenuItem exit = new JMenuItem("Выход");
    private JMenuItem matrixGraph = new JMenuItem("По матрице смежности");

    private Dinic dinic;

    private File graphFile;
    /**
     * Считывает граф из файла
     * Формат файла:
     * N
     * s t
     * a11 a12 .. a1n
     * ..............
     * an1 an2 .. ann
     * где a - веса
     * @param file файл с графом .txt
     * @return функцию пропускной способности
     * @throws FileNotFoundException
     */
    private int[][] readGraph(File file, int s, int t) throws FileNotFoundException {
        int [][]c;
        int size;
        Scanner sc = new Scanner(file);
        size = new Integer(sc.next());
        s= new Integer(sc.next());
        t= new Integer(sc.next());
        c= new int[size][size];
        for(int i=0;i<size;++i)
            for (int j = 0; j < size; j++)
                c[i][j]=new Integer(sc.next());
        return c;
    }

    /**
     * Слушатель для кнопки выбора способа представления графа
     */
    private class FileChooserListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooseFile = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Text files", "txt");
            chooseFile.setFileFilter(filter);
            int ret= chooseFile.showDialog(null, "Ok");
            if (ret == JFileChooser.APPROVE_OPTION) graphFile = chooseFile.getSelectedFile();
            int s=0,t=0;
            if(e.getActionCommand().equals("matrix")){
                try {
                    dinic = new Dinic(readGraph(graphFile,s,t),s,t);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
            //else if(e.getActionCommand().equals(""))
        }
    }

    public ThePilgrim(){
        //  Инициализакция окна программы
        super("Revolve");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int sizeWidth = 900;
        int sizeHeight = 900;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        setBounds(locationX, locationY, sizeWidth, sizeHeight);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(sizeWidth,sizeHeight);

        // Стиль ОС
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // меню
        mainMenu.add(file);
        setJMenuBar(mainMenu);

        // файл
        file.add(openGraphFromFile);
        file.add(new JSeparator());
        file.add(exit);

        // кнопка выхода
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // граф по матрице смежности
        matrixGraph.addActionListener(new FileChooserListener());
        matrixGraph.setActionCommand("matrix");
        // открытие графа из файла
        openGraphFromFile.add(matrixGraph);

    }

    public static void main(String args[]){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ThePilgrim();
            }
        });
    }
}
