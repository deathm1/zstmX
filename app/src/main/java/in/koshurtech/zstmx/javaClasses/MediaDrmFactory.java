package in.koshurtech.zstmx.javaClasses;

import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;

public class MediaDrmFactory {

    /* renamed from: a */
    public static final UUID f46a = new UUID(6801168765294362723L, -8541191043631480108L);

    /* renamed from: b */
    public static final UUID f47b = new UUID(-7348484286925749626L, -6083546864340672619L);

    /* renamed from: c */
    public static final UUID f48c = new UUID(-7338653513101981915L, -8305690818819724279L);

    /* renamed from: d */
    public static final UUID f49d = new UUID(-1301668207276963122L, -6645017420763422227L);

    @Retention(RetentionPolicy.SOURCE)
    public @interface Provider {
    }

    /* renamed from: a */
    public static MediaDrm m45a(int i) throws Exception{
        switch (i) {
            case 0:
                try {
                    return new MediaDrm(f47b);
                } catch (UnsupportedSchemeException e) {
                    return null;
                }
            case 1:
                return new MediaDrm(f49d);
            case 2:
                return new MediaDrm(f46a);
            case 3:
                return new MediaDrm(f48c);
            default:
                return null;
        }
    }
}
