import java.util.ArrayList;

import static java.lang.Integer.min;

/**
 * Класс для работы с ТС по алгортиму Диница
 */
public class Dinic {
    // число вершин
    private int N;
    // функция пропускной способности и функция нагрузки
    private int[][] c, f;

    private int[] d, q;

    public Dinic(int[][] c){
        this.c=c;
        N=c.length;
        f= new int[N][N];
        d=new int[N];
        q = new int[N];
    }

    private boolean bfs() {
        int qh=0, qt=0;
        q[qt++] = 0;
        for(int i=1;i<N;++i) d[i]=-1;
        while (qh < qt) {
            int v = q[qh++];
            for (int to=0; to<N; ++to)
                if (d[to] == -1 && f[v][to] < c[v][to]) {
                    q[qt++] = to;
                    d[to] = d[v] + 1;
                }
        }
        return d[N-1] != -1;
    }

    // поиском в глубину ищет блокируещие потоки
    private int dfs (int v, int flow) {
        if (v == N-1)  return flow;
        if (flow!=0) {
            for (int to = 0; to < N; ++to) {
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
            int pushed;
            while ( (pushed = dfs (0, 1000000000))!=0) flow += pushed;
        }
        return flow;
    }
}
