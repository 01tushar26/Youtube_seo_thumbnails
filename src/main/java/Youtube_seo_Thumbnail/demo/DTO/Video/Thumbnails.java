package Youtube_seo_Thumbnail.demo.DTO.Video;

import lombok.Data;

@Data
public class Thumbnails {
    private Thumbnail maxres;
    private Thumbnail high;
    private Thumbnail medium;
    private Thumbnail _default;

   public String getBestThumbnailUrl() {
        if (maxres != null) return maxres.getUrl();
        if (high != null) return high.getUrl();
        if (medium != null) return medium.getUrl();
        return _default != null ? _default.getUrl() : "";
    }
}
