const NEXT_PUBLIC_API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

export const apiFetch = (url: string, options?: RequestInit) => {
  if (options?.body) {
    const headers = new Headers(options?.headers || {});

    if (!headers.has("Content-Type")) {
      headers.set("Content-Type", "application/json; charset=utf-8");
    }

    options.headers = headers;
  }

  return fetch(`${NEXT_PUBLIC_API_BASE_URL}${url}`, {
    credentials: "include",
    ...options
  }).then((res) => {
    if (!res.ok) {
      return res.json().then((errorData) => {
        throw errorData;
      });
    }

    return res.json();
  });
};
