package pl.allegro.promo.devoxx2015.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.allegro.promo.devoxx2015.domain.Offer;
import pl.allegro.promo.devoxx2015.domain.OfferRepository;
import pl.allegro.promo.devoxx2015.domain.PhotoScoreSource;

import java.util.List;

@Component
public class OfferService {

    private static final Logger log = LoggerFactory.getLogger(OfferService.class);

    private static final Sort SORT_SCORE_DESC = new Sort(Sort.Direction.DESC, "photoScore");

    private final OfferRepository offerRepository;
    private final PhotoScoreSource photoScoreSource;

    @Autowired
    public OfferService(OfferRepository offerRepository, PhotoScoreSource photoScoreSource) {
        this.offerRepository = offerRepository;
        this.photoScoreSource = photoScoreSource;
    }

    public void processOffers(List<OfferPublishedEvent> events) {
        events
                .stream()
                .map(this::toOffer)
                .filter(Offer::hasPrettyPhoto)
                .forEach(offerRepository::save);
    }

    private Offer toOffer(OfferPublishedEvent e) {
        return new Offer(e.getId(), e.getTitle(), e.getPhotoUrl(), tryFindScore(e));
    }

    private double tryFindScore(OfferPublishedEvent e) {
        try {
            return photoScoreSource.getScore(e.getPhotoUrl());
        } catch (Exception ex) {
            log.warn("Score not available, assuming {}", Offer.PRETTY_THRESHOLD, ex);
            return Offer.PRETTY_THRESHOLD;
        }
    }

    public List<Offer> getOffers() {
        return offerRepository.findAll(SORT_SCORE_DESC);
    }
}
