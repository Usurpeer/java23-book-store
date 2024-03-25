import { autoFormat } from "../../_js/helpers.js";

export function setRows(table, rows) {
  const tableBody = table.querySelector("tbody");
  tableBody.replaceChildren();
  for (const row of rows) {
    tableBody.append(createRow(row));
  }
}

function createRow(row) {
  const tr = document.createElement("tr");

  for (const field in row) {
    // для перехода по id
    if (field == "id") {
      tr.addEventListener("click", () => {
        window.location.href = `order/index.html?id=${row.id}`;
      });
    }
    tr.append(createCell(field, row[field]));
  }
  return tr;
}

function createCell(field, value) {
  const td = document.createElement("td");
  if (typeof value === "number") {
    td.classList.add("td-number");
  }
  td.classList.add("clickable");

  td.innerText = autoFormat(
    value,
    field.match(/quantity|count|id/gi) && "integer"
  );

  return td;
}

export function showSortBy(sortOption, sorting) {
  sortOption.forEach((s) => {
    const si = s.querySelector(".bi");
    si.classList.remove("bi-sort-down-alt");
    si.classList.remove("bi-sort-up-alt");

    if (s.getAttribute("sortBy") == sorting.field) {
      si.classList.add(sorting.asc ? "bi-sort-down-alt" : "bi-sort-up-alt");
    }
  });
}
