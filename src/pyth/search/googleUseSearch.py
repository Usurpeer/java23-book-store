import tensorflow_hub as hub
from sklearn.metrics.pairwise import cosine_similarity
from collections import defaultdict

# видимо не подходит для русского языка
# Загрузка предварительно обученной модели Universal Sentence Encoder работает
use_model = hub.load("https://tfhub.dev/google/universal-sentence-encoder/4")
#use_model = hub.load("https://tfhub.dev/google/universal-sentence-encoder-multilingual/3")

# Функция для расчета косинусной схожести между запросом и текстом книги
def calculate_similarity(query_embedding, book_embedding):
    similarity_score = cosine_similarity([query_embedding], [book_embedding])[0][0]
    return similarity_score

# Функция поиска книги на основе запроса
def searching(query, books):
    relevance_map = defaultdict(float)
    # Получение векторного представления запроса
    query_embedding = use_model([query])[0]
    max_relevance = 0
    for book in books:
        # Получение векторного представления описания книги
        book_embedding = use_model([book['title'] + ' ' + book['description'] + ' '.join(book['authors']) + ' '.join(book['genres'])])[0]
        # Расчет схожести между запросом и книгой
        relevance = calculate_similarity(query_embedding, book_embedding)
        relevance_map[book['id']] = relevance
        if relevance > max_relevance:
            max_relevance = relevance
    # Нормировка результатов
    relevance_map = {book_id: float(relevance / max_relevance) for book_id, relevance in relevance_map.items()}
    # Фильтрация результатов
    filtered_results = {book_id: float(relevance) for book_id, relevance in relevance_map.items() if relevance > 0.6}
    return filtered_results
