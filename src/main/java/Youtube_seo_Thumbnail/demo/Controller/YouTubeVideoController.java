package Youtube_seo_Thumbnail.demo.Controller;


import Youtube_seo_Thumbnail.demo.Entity.VideoDetails;
import Youtube_seo_Thumbnail.demo.Service.ThumbnailService;
import Youtube_seo_Thumbnail.demo.Service.YouTubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
    @RequiredArgsConstructor
    public class YouTubeVideoController {

        private final YouTubeService youTubeService;
        private final ThumbnailService service;

        @GetMapping("/youtube/video-details")
        public String showVideoForm() {
            return "video-details"; // Thymeleaf template name
        }

        @PostMapping("/youtube/video-details")
        public String fetchVideoDetails(
                @RequestParam String videoUrlOrId,
                Model model) {

            String videoId = service.extractVideoId(videoUrlOrId);

            if (videoId == null) {
                model.addAttribute("error", "Invalid YouTube URL or ID.");
                return "video-details";
            }

            VideoDetails details = youTubeService.getVideoDetails(videoId);

            if (details == null) {
                model.addAttribute("error", "Video not found.");
            } else {
                model.addAttribute("videoDetails", details);
            }

            model.addAttribute("videoUrlOrId", videoUrlOrId);
            return "video-details";
        }
    }


