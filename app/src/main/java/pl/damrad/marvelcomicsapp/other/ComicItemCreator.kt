package pl.damrad.marvelcomicsapp.other

import pl.damrad.marvelcomicsapp.adapters.items.ComicsItem
import pl.damrad.marvelcomicsapp.retrofit.response.MarvelResponse

object ComicItemCreator {
    fun createComicItem(marvelResponse: MarvelResponse): ArrayList<ComicsItem?> {
        val list = marvelResponse.data?.results
        val comicsList = ArrayList<ComicsItem?>()
        if (list != null) {
            for (item in list) {

                val comicAuthors: StringBuilder = StringBuilder("written by ")
                item?.creators?.let { creators ->
                    creators.items?.let { list ->
                        for (author in list) {
                            author?.name?.let { name ->
                                if (author.role == "writer") {
                                    comicAuthors.append("$name, ")
                                }
                            }
                        }
                    }
                }

                var detailPath = ""
                for (path in item?.urls?.iterator()!!) {
                    path?.type?.let { type ->
                        if (type == "detail") {
                            path.url?.let { url ->
                                detailPath = url
                            }
                        }
                    }
                }

                val comics = ComicsItem(
                    title = item.title.toString(),
                    description = item.description ?: "",
                    author = if (comicAuthors.length > 11) comicAuthors.substring(0, comicAuthors.lastIndex - 1) else "",
                    imagePath = "${item.thumbnail?.path}/portrait_uncanny.${item.thumbnail?.extension}".replace("http", "https"),
                    morePath = detailPath
                )
                comicsList.add(comics)
            }
        }
        return comicsList
    }
}