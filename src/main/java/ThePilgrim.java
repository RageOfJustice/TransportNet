import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ThePilgrim extends JFrame {

    private JMenuBar mainMenu = new JMenuBar();

    private JMenu file = new JMenu("Файл");

    private JMenuItem openGraphFromFile = new JMenuItem("Загрузить граф");
    private JMenuItem exit = new JMenuItem("Выход");

    private JPanel contentPane;

    private JLabel maxThreadLabel;

    private JTextArea graphView= new JTextArea();

    private JComboBox<String> algorithmBox = new JComboBox<>();

    private JButton findMaxNet = new JButton("Найти");

    private int [][]c;

    /**
     * Считывает граф из файла
     * Формат файла
     * N
     * a bi ci
     * .........
     * an bi ci
     * нумерация вершин с 0
     * i = {0,..,N}
     * a - вершина начало ребра
     * b - вершина конец ребра
     * с - вес или функция пропускной способности
     * все переменные разные
     * @param file файл с графом .txt
     * @return функция пропускной способности
     * @throws FileNotFoundException
     */
    public static int[][] readGraph(File file) throws FileNotFoundException {
        int[][] c;
        int size;
        Scanner sc = new Scanner(file);
        size = new Integer(sc.next());
        c = new int[size][size];
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (!line.isEmpty()) {
                StringTokenizer st = new StringTokenizer(line);
                int begin = new Integer(st.nextToken());
                while (st.hasMoreTokens())
                    c[begin][new Integer(st.nextToken())] = new Integer(st.nextToken());
            }
        }
        return c;
    }

    /**
     * Слушатель для кнопки выбора способа представления графа
     */
    private class FileChooserListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooseFile = new JFileChooser();
            chooseFile.setCurrentDirectory(new File("src/main/resources"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Text files", "txt");
            chooseFile.setFileFilter(filter);
            int ret = chooseFile.showDialog(null, "Ok");
            if (ret == JFileChooser.APPROVE_OPTION) {
                if(maxThreadLabel!=null)
                    contentPane.remove(maxThreadLabel);
                File graphFile = chooseFile.getSelectedFile();
                try {
                    graphView.setText(readFile(graphFile));
                    c = readGraph(graphFile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                revalidate();
                repaint();
            }
        }
    }

    private String readFile(File file) throws IOException {
        BufferedReader bf = new BufferedReader(new FileReader(file));
        StringBuilder res = new StringBuilder();
        String s;
        while ((s = bf.readLine()) != null) {
            res.append(s);
            res.append("\n");
        }
        return res.toString();
    }

    /**
     * Создает новую панель с менеджером BoxLayout по нужному направлению,
     * отступами и самими компонентами
     * @param strut величина отступа
     * @param axisX true - если по оси X, false - по Y
     * @param components компоненты для добваления
     * @return заполненная панель
     */
    private JPanel getPanel(int strut,boolean axisX,JComponent... components){
        JPanel panel = new JPanel();
        if(axisX) panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
        else panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        for(JComponent component: components){
            panel.add(component);
            if(axisX) panel.add(Box.createHorizontalStrut(strut));
            else panel.add(Box.createVerticalStrut(strut));
        }
        return panel;
    }

    public ThePilgrim(){
        //  Инициализакция окна программы
        super("Revolve");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int sizeWidth = 400;
        int sizeHeight = 400;
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
        exit.addActionListener(e -> System.exit(0));

        // открытие графа из файла
        openGraphFromFile.addActionListener(new FileChooserListener());

        // панель с контентом
        JPanel buttons = getPanel(20,true,algorithmBox,findMaxNet);
        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPane = getPanel(0,false,new JScrollPane(graphView),buttons);
        setContentPane(contentPane);

        // вывод графа из файлa
        graphView.setLineWrap(true);
        graphView.setAlignmentX(Component.CENTER_ALIGNMENT);
        graphView.setEditable(false);


        // выбор алгоритма
        algorithmBox.setMaximumSize(new Dimension(100,25));
        algorithmBox.addItem("Диниц");
        algorithmBox.addItem("Эдмондс-Карп");

        // кнопка выполнить
        findMaxNet.setMaximumSize(new Dimension(100,25));
        findMaxNet.addActionListener(e -> {
            if(maxThreadLabel!=null)
                contentPane.remove(maxThreadLabel);
            int maxThread=0;
            if(algorithmBox.getSelectedItem().equals("Диниц")){
                maxThread = new Dinic(c).dinic();
            }
            else if(algorithmBox.getSelectedItem().equals("Эдмондс-Карп")){
                maxThread = new EdmondsKarp(c).edmondsKarp();
            }
            maxThreadLabel= new JLabel("Максимальный поток = " + maxThread);
            maxThreadLabel.setAlignmentX(CENTER_ALIGNMENT);
            contentPane.add(maxThreadLabel);
            repaint();
            revalidate();
        });

    }

    public static void main(String args[]){
        EventQueue.invokeLater(ThePilgrim::new);
    }
}
