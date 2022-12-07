package ru.practicum.ewm.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.hit.dto.CreateStatDto;
import ru.practicum.ewm.hit.dto.ReturnStatDto;
import ru.practicum.ewm.hit.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.ewm.hit.mapper.StatMapper.toStats;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Override
    public void addStat(CreateStatDto createStat) {
        statRepository.save(toStats(createStat));
    }

    @Override
    public List<ReturnStatDto> get(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime starts = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime ends = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (uris.size() == 1 && uris.get(0).equals("/events")) {
            List<ReturnStatDto> getasdf = statRepository.getStatForAllEvents();

            for (ReturnStatDto rsd : getasdf) {
                if (rsd.getUri().length() == 7) {
                    getasdf.remove(rsd);
                    break;
                }
            }

            System.out.println(getasdf + " " + start + " " + end + " " + uris);
            return getasdf;
        }

        if (unique) {
            return statRepository.getAllUniqueIp(starts, ends, uris);
        } else {
            List<ReturnStatDto> getasdf = statRepository.getAll(starts, ends, uris);
            System.out.println(getasdf + " " + start + " " + end + " " + uris);
            return getasdf;
        }
    }
}