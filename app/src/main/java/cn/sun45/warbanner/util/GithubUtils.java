package cn.sun45.warbanner.util;

/**
 * Created by Sun45 on 2021/11/15
 * Github类
 */
public class GithubUtils {
    //GitHub Pages
    public static final int TYPE_GITHUBPAGES =0;
    //cloudflare
    public static final int TYPE_CLOUDFLARE = 1;
    //JsDeliver
    public static final int TYPE_JSDELIVR = 2;
    //Github Raw
    public static final int TYPE_RAW = 3;

    public static final String getFileUrl(int type, String owner, String repository, String path) {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case TYPE_GITHUBPAGES:
                sb.append("https://");
                sb.append(owner);
                sb.append(".github.io");
                sb.append("/");
                sb.append(repository);
                sb.append("/");
                sb.append(path);
                break;
            case TYPE_CLOUDFLARE:
                sb.append("https://round-union-edb0.sun45.workers.dev/");
                sb.append("/");
                sb.append(getFileUrl(TYPE_RAW, owner, repository, path));
                break;
            case TYPE_JSDELIVR:
                sb.append("https://cdn.jsdelivr.net/gh/");
                sb.append(owner);
                sb.append("/");
                sb.append(repository);
                sb.append("@master");
                sb.append("/");
                sb.append(path);
                break;
            case TYPE_RAW:
                sb.append("https://raw.githubusercontent.com/");
                sb.append(owner);
                sb.append("/");
                sb.append(repository);
                sb.append("/master/");
                sb.append(path);
                break;
            default:
                break;
        }
        return sb.toString();
    }
}
