package com.example.myapplication.model.repository;

import androidx.annotation.NonNull;

public class TodoItemSearchCriteria {
    public long todoListId = -1;

    public String searchKeyword;
    @NonNull
    public CompletionState completionState = CompletionState.NotSpecified;
    @NonNull
    public ExpiryState expiryState = ExpiryState.NotSpecified;
    @NonNull
    public OrderBy orderBy = OrderBy.CreateDate;

    public enum CompletionState {
        NotSpecified,
        Complete,
        Incomplete
    }

    public enum ExpiryState {
        NotSpecified,
        Expired,
        NotExpired
    }

    public enum OrderBy {
        CreateDate,
        Deadline,
        Name,
        Status
    }
}
