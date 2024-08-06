import tensorflow_hub as hub
import numpy as np

# Загружаем предварительно обученную модель Universal Sentence Encoder (USE)
# https://tfhub.dev/google/universal-sentence-encoder/4 нет
# https://www.kaggle.com/models/google/universal-sentence-encoder/TensorFlow2/multilingual/2 нет
# https://www.kaggle.com/models/google/universal-sentence-encoder/TensorFlow2/universal-sentence-encoder/2 рабочая
module_url = "https://www.kaggle.com/models/google/universal-sentence-encoder/TensorFlow2/universal-sentence-encoder/2"
model = hub.load(module_url)

# Функция для вычисления косинусного расстояния между векторами
def cosine_similarity(v1, v2):
    mag1 = np.linalg.norm(v1)  # Вычисляем длину вектора v1
    mag2 = np.linalg.norm(v2)  # Вычисляем длину вектора v2
    if (mag1 == 0 or mag2 == 0):
        return 0
    return np.dot(v1, v2) / (mag1 * mag2)  # Возвращаем косинусное расстояние между v1 и v2

# Функция для вычисления релевантности поискового запроса к книге
def compute_relevance(query_embedding, book_embedding):
    return cosine_similarity(query_embedding, book_embedding)  # Возвращаем релевантность запроса к книге

# Функция для создания эмбеддинга текста на основе USE
def get_text_embedding(text):
    return model([text])[0]  # Возвращаем эмбеддинг текста

# Функция для выполнения интеллектуального поиска
def searching(query, books):
    query_embedding = get_text_embedding(query)  # Получаем эмбеддинг запроса

    relevance_map = {}  # Инициализируем пустой словарь релевантности книг

    for book in books:
        book_id = book['id']  # Получаем идентификатор книги
        title_embedding = get_text_embedding(book['title'])  # Получаем эмбеддинг заголовка книги
        description_embedding = get_text_embedding(book['description'])  # Получаем эмбеддинг описания книги
        publisher_embedding = get_text_embedding(book['publisher'])

        # Вычисляем релевантность каждого параметра книги к запросу
        title_relevance = compute_relevance(query_embedding, title_embedding)
        description_relevance = compute_relevance(query_embedding, description_embedding)
        publisher_relevance = compute_relevance(query_embedding, publisher_embedding)

        # Вычисляем релевантность каждого автора к запросу
        authors_relevance = sum([compute_relevance(query_embedding, get_text_embedding(author)) for author in book['authors']])
        # Вычисляем релевантность каждого жанра к запросу
        genres_relevance = sum([compute_relevance(query_embedding, get_text_embedding(genre)) for genre in book['genres']])


        # Взвешиваем релевантности параметров книги соответствующими коэффициентами
        title_weight = 1
        description_weight = 0.7
        authors_weight = 1
        genres_weight = 1
        publisher_weight = 1

        # Суммируем взвешенные релевантности для получения общей релевантности книги
        total_relevance = (title_weight * title_relevance +
                           description_weight * description_relevance +
                           authors_weight * authors_relevance +
                           genres_weight * genres_relevance +
                           publisher_weight * publisher_relevance)

        relevance_map[book_id] = total_relevance  # Сохраняем общую релевантность в словаре

    # Нормализация результатов
    max_relevance = max(relevance_map.values())  # Находим максимальное значение релевантности
    for book_id, relevance in relevance_map.items():
        relevance_map[book_id] = relevance / max_relevance  # Нормируем релевантность

    # Фильтрация по релевантности > 0.7
    relevance_map = {book_id: relevance for book_id, relevance in relevance_map.items() if relevance > 0.7}

    return relevance_map  # Возвращаем отфильтрованные результаты релевантности книг