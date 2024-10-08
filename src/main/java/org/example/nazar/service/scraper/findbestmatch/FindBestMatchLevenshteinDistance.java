package org.example.nazar.service.scraper.findbestmatch;

import org.example.nazar.dto.BaseDTO;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

@Service
public class FindBestMatchLevenshteinDistance<T extends BaseDTO> implements IFindBestMatch<T> {


    @Override
    public Optional<T> finder(List<T> dtoList, String search) {
        LevenshteinDistance distance = new LevenshteinDistance();
        //حذف اخرین عنصر
        dtoList.remove(dtoList.size() - 1);
        return dtoList.
                stream().
                min(Comparator.
                        comparingInt
                                (dto -> distance.
                                        apply
                                                (dto.getTitle().toLowerCase(),
                                                        search.toLowerCase())));
    }


}
