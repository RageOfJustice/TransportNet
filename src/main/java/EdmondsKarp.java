import static java.lang.Integer.min;

/**
 * Класс для работы с ТС по алгортиму Едмондса-Карпа
 */
public class EdmondsKarp{
    // число вершин
    private int N;
    // функция пропускной способности и функция нагрузки
    private int[][] c, f;

    private int[] path, q;


    public EdmondsKarp(int[][] c){
        this.c=c;
        N=c.length;
        f= new int[N][N];
        q = new int[N];
        path = new int[N];
    }

    private boolean bfs(){
        int qh=0, qt=0;
        q[qt++] = 0;
        while(qh<qt) {
            int v = q[qh++];
            for (int to = 0; to < N; to++)
                if (path[to] == -1 && f[v][to] < c[v][to]) {
                    q[qt++] = to;
                    path[to] = v;
                    if (path[N - 1] != -1) return true;
                }
        }
        return false;

    }

    private int getMinCap(){
        int cap = 1000000000;
        for (int cur=N-1; cur!=0; ){
            int prev = path[cur];
            cap = min (cap, c[prev][cur]-f[prev][cur]);
            cur = prev;
        }
        return cap;
    }

    private void changePathFlow(){
        int cap = getMinCap();
        for (int cur=N-1; cur!=0; )
        {
            int prev = path[cur];
            f[prev][cur] += cap;
            f[cur][prev] -= cap;
            cur = prev;
        }
    }

    public int edmondsKarp(){
        int flow = 0;
        while(true){
            for(int i=1;i<N;++i) path[i]=-1;
            if(!bfs()) break;
            changePathFlow();
        }
        for (int i=0; i<N; ++i)
            if (c[0][i]!=0)
                flow += f[0][i];
        return flow;
    }

}
