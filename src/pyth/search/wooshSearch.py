import tempfile
from whoosh.fields import Schema, TEXT, ID, NUMERIC
from whoosh.index import create_in
from whoosh.qparser import MultifieldParser
from typing import List, Tuple, Dict, Any
from whoosh.index import open_dir

# является аналогично Java

def create_index(books: List[dict]) -> str:
    """
    Создает индекс Whoosh на основе списка книг.
    Возвращает путь к временной директории с индексом.
    """
    # Определяем схему индекса
    schema = Schema(
        id=ID(unique=True, stored=True),
        title=TEXT(stored=True),
        description=TEXT(stored=True),
        price=NUMERIC(float, stored=True),
        publisher=TEXT(stored=True),
        year=NUMERIC(int, stored=True),
        authors=TEXT(stored=True, phrase=True),
        genres=TEXT(stored=True, phrase=True)
    )

    # Создаем временную директорию для индекса
    temp_dir = tempfile.mkdtemp()

    # Создаем или открываем индекс во временной директории
    ix = create_in(temp_dir, schema)

    # Получаем объекты писателя и средства для записи в индекс
    writer = ix.writer()

    # Проходимся по каждой книге и индексируем ее
    for book in books:
        writer.add_document(
            id=str(book["id"]),
            title=book["title"],
            description=book["description"],
            price=book["price"],
            publisher=book["publisher"],
            year=book["year"],
            authors=" ".join(book["authors"]),
            genres=" ".join(book["genres"])
        )

    # Завершаем запись в индекс
    writer.commit()

    return temp_dir


def searching(query: str, books: List[dict]) -> Dict[int, Any]:
    """
    Выполняет поиск по строке запроса в списке книг.
    Возвращает результаты поиска в формате {id: relevance}.
    """
    # Создаем индекс на основе списка книг
    index_dir = create_index(books)

    # Открываем индекс для поиска
    ix = open_dir(index_dir)

    # Выполняем поиск
    with ix.searcher() as searcher:
        # Создаем парсер для поискового запроса
        parser = MultifieldParser(["title", "description", "publisher", "authors", "genres"], schema=ix.schema)
        # Преобразуем строку запроса в объект запроса Whoosh
        query = parser.parse(query)
        # Выполняем поиск
        results = searcher.search(query)
        # Возвращаем результаты поиска в формате {id: relevance}
        return {int(hit["id"]): hit.score for hit in results}
