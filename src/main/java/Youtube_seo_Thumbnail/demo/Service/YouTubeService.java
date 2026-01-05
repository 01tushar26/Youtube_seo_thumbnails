package Youtube_seo_Thumbnail.demo.Service;

import Youtube_seo_Thumbnail.demo.DTO.Video.Thumbnail;
import Youtube_seo_Thumbnail.demo.DTO.Video.Thumbnails;
import Youtube_seo_Thumbnail.demo.Entity.SearchVideo;
import Youtube_seo_Thumbnail.demo.Entity.Video;
import Youtube_seo_Thumbnail.demo.Entity.VideoDetails;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YouTubeService {


    public final WebClient.Builder webclientBuilder;


    @Value("${youtube.api.key}")
    private String apiKey;

    @Value("${youtube.api.base.url}")
    private String baseUrl;

    @Value("${youtube.api.max.related.videos}")
    private int maxRelated;

    public SearchVideo searchVideos(String videoTitle) {

        List<String> videoIds=searchForVideoIds(videoTitle);

        if(videoIds.isEmpty()){
            return SearchVideo.builder()
                    .primaryVideo(null)
                    .relatedVideos(Collections.emptyList())
                    .build();
        }
        String primaryvideoid= videoIds.get(0);
        List<String> relatedvideoid = videoIds.subList(1,Math.min(videoIds.size(), maxRelated));
        Video primaryvideo = getVideoById(primaryvideoid);
        List<Video> relatedvideo = new ArrayList<>();
        for(String id :relatedvideoid){
            Video video =  getVideoById(id);
            if(video!=null){
                relatedvideo.add(video);
            }

        }
        return SearchVideo.builder().primaryVideo(primaryvideo).relatedVideos(relatedvideo).build();

    }
    private Video getVideoById(String videoId) {

        VideoApiResponse response = webclientBuilder
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part", "snippet")
                        .queryParam("id", videoId)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();

        if (response == null || response.getItems() == null) {
           return null;
        }
        Snippet snippet = response.items.get(0).snippet;

        return Video.builder()
                .id(videoId)
                .channelTitle(snippet.channelTitle)
                .title(snippet.title)
                .tags(snippet.tags == null
                        ? Collections.emptyList()
                        : snippet.tags)
                .build();

    }


    private List<String> searchForVideoIds(String videoTitle) {
       SearchApiResponse response =webclientBuilder.baseUrl(baseUrl).build()
               .get()
               .uri(uriBuilder -> uriBuilder
                       .path("/search")
                       .queryParam("part","snippet")
                       .queryParam("q",videoTitle)
                       .queryParam("type","video")
                       .queryParam("maxResult",maxRelated)
                       .queryParam("key",apiKey)
                       .build())
               .retrieve()
               .bodyToMono(SearchApiResponse.class)
               .block();

       if(response == null || response.items==null){
           return Collections.emptyList();
       }
       List<String> videoIds = new ArrayList<>();
       for(SearchItem item : response.items){
           videoIds.add(item.id.videoId);
       }
        return videoIds;
    }
    public VideoDetails getVideoDetails(String videoId) {

        VideoApiResponse response = webclientBuilder
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/videos")
                        .queryParam("part", "snippet")
                        .queryParam("id", videoId)
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(VideoApiResponse.class)
                .block();

        if (response == null || response.items.isEmpty()) {
            return null;
        }

        Snippet snippet = response.items.get(0).snippet;
        String thumbnailUrl = snippet.thumbnails.getBestThumbnailUrl();

        return VideoDetails.builder()
                .id(videoId)
                .title(snippet.title)
                .description(snippet.description)
                .tags(snippet.tags == null ? Collections.emptyList() : snippet.tags)
                .thumbnailUrl(thumbnailUrl)
                .channelTitle(snippet.channelTitle)
                .publishedAt(snippet.publishedAt)
                .build();
    }

    @Data
     static class SearchApiResponse {
        private List<SearchItem> items;
    }

    @Data
     static class SearchItem {
        private Id id;
    }



    @Data
     static class Id {
        private String videoId;
    }
    @Data
    static class VideoApiResponse {
        private List<VideoItem> items;
    }

    @Data
    static class VideoItem {
        private Snippet snippet;
    }

    @Data
    static class Snippet {
        private String title;
        private String description;
        private String channelTitle;
        private String publishedAt;
        private List<String> tags;
        private Thumbnails thumbnails;
    }


}
