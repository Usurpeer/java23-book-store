package ru.teamscore.java23.books.config;

//@Configuration
public class ModelMapperConfig {
   /* @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        // сопоставления Book и CatalogBookDto
        TypeMap<Book, CatalogBookDto> typeMapCatalogBook = modelMapper.createTypeMap(Book.class, CatalogBookDto.class);
        typeMapCatalogBook.addMappings(mapping -> {
            mapping.map(Book::getAuthors, CatalogBookDto::setAuthors);
            mapping.map(Book::getGenres, CatalogBookDto::setGenres);
        });
        TypeMap<Book, BookPageDto> typeMapBook = modelMapper.createTypeMap(Book.class, BookPageDto.class);
        typeMapBook.addMappings(mapping -> {
            mapping.map(Book::getAuthors, BookPageDto::setAuthors);
            mapping.map(Book::getGenres, BookPageDto::setGenres);
            mapping.map(Book::getPublisher, BookPageDto::setPublisher);
        });

        // Orders - сопоставления Order и CustomerOrderDto
        Converter<LocalDateTime, LocalDate> localDateTimeToLocalDateConverter = context ->
                context.getSource() == null ? null : context.getSource().toLocalDate();
        Converter<OrderStatus, String> orderStatusStringConverter = context ->
                context.getSource() == null ? null : context.getSource().getTitle();
        TypeMap<Order, CustomerOrderDto> typeMapActiveOrder = modelMapper.createTypeMap(Order.class, CustomerOrderDto.class);
        typeMapActiveOrder.addMappings(mapping -> {
            mapping.using(localDateTimeToLocalDateConverter).map(Order::getCreated, CustomerOrderDto::setCreated);
            mapping.using(orderStatusStringConverter).map(Order::getStatus, CustomerOrderDto::setStatus);

        });
        typeMapActiveOrder.addMapping(Order::getTotalAmount, CustomerOrderDto::setAmount);
        typeMapActiveOrder.addMapping(Order::getTotalQuantity, CustomerOrderDto::setQuantityBooks);

        modelMapper.addConverter(orderStatusStringConverter);

        return modelMapper;
    }*/
}
