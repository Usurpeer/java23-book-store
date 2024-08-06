from transformers import BertTokenizer, BertModel
import torch
from sklearn.metrics.pairwise import cosine_similarity
from collections import defaultdict

# Загрузка предварительно обученной модели RuBERT
tokenizer = BertTokenizer.from_pretrained("DeepPavlov/rubert-base-cased")
model = BertModel.from_pretrained("DeepPavlov/rubert-base-cased")

# Функция для получения векторного представления текста
def get_text_embedding(text):
    input_ids = torch.tensor(tokenizer.encode(text, add_special_tokens=True)).unsqueeze(0)  # Преобразуем текст в токены
    outputs = model(input_ids)  # Подаем токены на вход модели
    last_hidden_states = outputs[0]  # Получаем последние скрытые состояния
    return last_hidden_states.mean(dim=1).squeeze().detach().numpy()  # Усредняем скрытые состояния для получения одного вектора

# Функция для расчета косинусной схожести между двумя векторами
def calculate_similarity(query_embedding, book_embedding):
    return cosine_similarity([query_embedding], [book_embedding])[0][0]

# Функция поиска книги на основе запроса
def searching(query, books):
    relevance_map = defaultdict(float)
    # Получение векторного представления запроса
    query_embedding = get_text_embedding(query)
    max_relevance = 0
    for book in books:
        # Получение векторного представления описания книги
        book_embedding = get_text_embedding(book['title'] + ' ' + book['description'] + ' '.join(book['authors']) + ' '.join(book['genres']))
        # Расчет схожести между запросом и книгой
        relevance = calculate_similarity(query_embedding, book_embedding)
        relevance_map[book['id']] = relevance
        if relevance > max_relevance:
            max_relevance = relevance
    # Нормировка результатов
    relevance_map = {book_id: relevance / max_relevance for book_id, relevance in relevance_map.items()}
    # Фильтрация результатов
    filtered_results = {book_id: relevance for book_id, relevance in relevance_map.items() if relevance > 0.6}
    return filtered_results
