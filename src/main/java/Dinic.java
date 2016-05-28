import static java.lang.Integer.min;

/**
 * Класс для работы с ТС по алгортиму Диница
 */
public class Dinic {
    // число вершин
    private int N;
    // константа-бесконечность
    private final int INF = 1000000000;
    // функция пропускной способности и функция нагрузки
    int[][] c, f;

    int[] d, ptr,q;

    int s, t;

    public Dinic(int[][] c, int s, int t){
        N=c.length;
        this.c=c;
        this.s=s;
        this.t=t;
        f= new int[N][N];
        d=new int[N];
        ptr = new int[N];
        q = new int[N];
    }

    private boolean bfs() {
        int qh=0, qt=0;
        q[qt++] = s;
        for(int el: d) el=-1;
        d[s] = 0;
        while (qh < qt) {
            int v = q[qh++];
            for (int to=0; to<N; ++to)
                if (d[to] == -1 && f[v][to] < c[v][to]) {
                    q[qt++] = to;
                    d[to] = d[v] + 1;
                }
        }
        return d[t] != -1;
    }

    // поиском в глубину ищет блокируещие потоки
    private int dfs (int v, int flow) {
        if (v == t)  return flow;
        if (flow!=0) {
            for (int to = ptr[v]; to < N; ++to) {
                if (d[to] != d[v] + 1) continue;
                int pushed = dfs(to, min(flow, c[v][to] - f[v][to]));
                if (pushed != 0) {
                    f[v][to] += pushed;
                    f[to][v] -= pushed;
                    return pushed;
                }
            }
        }
        return 0;
    }

    // функция вычисляет максимальный поток
    public int dinic() {
        int flow = 0;
        while(true) {
            if (!bfs())  break;
            for(int el:ptr) el=0;
            int pushed;
            while ( (pushed = dfs (s, INF))!=0) flow += pushed;
        }
        return flow;
    }
}
