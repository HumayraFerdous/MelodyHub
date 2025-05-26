import com.melodyhub.model.Song;
import java.util.*;
import java.util.stream.Collectors;

public class RecommendationService {
    public List<Song> recommend(List<Song> allSongs, Stack<Song> history) {
        if (history.isEmpty()) return Collections.emptyList();

        Song lastPlayed = history.peek();
        String genre = lastPlayed.getGenre();
        String artist = lastPlayed.getArtist();

        return allSongs.stream()
                .filter(s -> !s.equals(lastPlayed) &&
                             (s.getGenre().equalsIgnoreCase(genre) ||
                              s.getArtist().equalsIgnoreCase(artist)))
                .limit(5)
                .collect(Collectors.toList());
    }
}
