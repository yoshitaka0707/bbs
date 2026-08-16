document.addEventListener("DOMContentLoaded", () => {
    const csrfToken = document
        .querySelector('meta[name="_csrf"]')
        .getAttribute("content");

    const csrfHeader = document
        .querySelector('meta[name="_csrf_header"]')
        .getAttribute("content");

    document.querySelectorAll(".like-button").forEach((button) => {
        button.addEventListener("click", async () => {
            const postId = button.dataset.postId;
            const likeCount =
                button.querySelector(".like-count");
            const likeError = button
                .closest(".post-actions")
                .querySelector(".like-error");

            button.disabled = true;
            likeError.hidden = true;

            try {
                const response = await fetch(
                    `/posts/${postId}/likes`,
                    {
                        method: "POST",
                        headers: {
                            [csrfHeader]: csrfToken
                        }
                    }
                );

                if (!response.ok) {
                    throw new Error(
                        `HTTP error: ${response.status}`
                    );
                }

                const data = await response.json();

                likeCount.textContent = data.likeCount;
                button.classList.toggle(
                    "liked",
                    data.liked
                );
                button.setAttribute(
                    "aria-pressed",
                    String(data.liked)
                );
            } catch (error) {
                console.error(
                    "いいね処理に失敗しました",
                    error
                );
                likeError.hidden = false;
            } finally {
                button.disabled = false;
            }
        });
    });
});