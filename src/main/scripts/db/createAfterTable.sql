--после всех остальных таблиц
create table "orders"."order_book" (
	"order_id" bigint,
	"book_id" bigint,
	"price" money not null,
	"quantity" int not null default 1,

	foreign key ("order_id")
		references "orders"."order"("id")
		on delete cascade,
	foreign key ("book_id")
		references "catalog"."book"("id")
		on delete restrict,
	primary key ("order_id", "book_id")
);