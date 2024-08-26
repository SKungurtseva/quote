package ru.syn.quotes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.syn.quotes.models.Quote;
import ru.syn.quotes.repositories.QuoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class QuoteService {

    @Autowired
    BashParser parser;
    @Autowired
    QuoteRepository repository;

    public List<Quote> getPage(int page) {
        List<Quote> ret = new ArrayList<>();
        Map<Integer, String> map = parser.getPage(page);
        for (var entry : map.entrySet()) {
            var rawQuote = new Quote(); // Новая цитата еще без id
            rawQuote.setQuoteId(entry.getKey());
            rawQuote.setText(entry.getValue());
            var existed = repository.findByQuoteIdEquals(rawQuote.getQuoteId());
            if (existed.isEmpty())
                ret.add(repository.save(rawQuote));
            else
                ret.add(existed.get());
        }
        return ret;
    }

    public Quote getById(int id) {
        var existingQuote = repository.findByQuoteIdEquals(id);
        if (existingQuote.isPresent())
            return existingQuote.get();
        var quoteEntry = parser.getById(id);
        if (quoteEntry == null) return null;
        var newQuote = new Quote();
        newQuote.setQuoteId(quoteEntry.getKey());
        newQuote.setText(quoteEntry.getValue());
        return repository.save(newQuote);
    }

    public Quote getRandom() {

        var quoteEntry = parser.getRandom();
        if (quoteEntry == null) return null;

        var existingQuote = repository.findByQuoteIdEquals(quoteEntry.getKey());
        if (existingQuote.isPresent())
            return existingQuote.get();

        var newQuote = new Quote();
        newQuote.setQuoteId(quoteEntry.getKey());
        newQuote.setText(quoteEntry.getValue());
        return repository.save(newQuote);
    }
}
